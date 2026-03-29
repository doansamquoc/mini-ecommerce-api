package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.shared.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import com.sam.miniecommerceapi.shared.exception.BusinessException;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductVariantRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductVariantUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.AttributeOption;
import com.sam.miniecommerceapi.product.entity.Category;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.product.helper.ProductMappingHelper;
import com.sam.miniecommerceapi.product.mapper.ProductMapper;
import com.sam.miniecommerceapi.product.mapper.ProductVariantMapper;
import com.sam.miniecommerceapi.product.repository.ProductRepository;
import com.sam.miniecommerceapi.product.repository.ProductVariantRepository;
import com.sam.miniecommerceapi.product.service.AttributeOptionService;
import com.sam.miniecommerceapi.product.service.CategoryService;
import com.sam.miniecommerceapi.product.service.ProductService;
import com.sam.miniecommerceapi.product.service.ProductVariantService;
import com.sam.miniecommerceapi.product.util.PriceUtils;
import com.sam.miniecommerceapi.product.util.SortingUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductMapper mapper;
    ProductRepository repository;
    CategoryService categoryService;
    ProductVariantMapper variantMapper;
    ProductVariantService variantService;
    AttributeOptionService optionService;
    ProductVariantRepository variantRepository;

    @Transactional
    @Override
    public ProductDetailsResponse createProduct(ProductCreationRequest r) {
        // Category
        Category category = categoryService.findById(r.getCategoryId());

        // Product
        if (existsBySlug(r.getSlug())) throw new BusinessException(ErrorCode.PRODUCT_SLUG_CONFLICT);
        Product product = mapper.toProduct(r);
        product.setCategory(category);

        // Why set minPrice here? Variants have saved yet? Because with all variants, these have to saved it.
        // If the update process by the variant have any bugs then @Transactional'll restore like never save product yet.
        product.setMinPrice(PriceUtils.calcMinPriceByRequest(r.getVariants()));

        // Save product before handle variants
        Product productSaved = repository.save(product);

        // Gather all skus in request
        List<String> skus = r.getVariants().stream().map(ProductVariantRequest::getSku).toList();

        // Check conflict sku in the request
        if (skus.size() != new HashSet<>(skus).size()) throw new BusinessException(ErrorCode.PRODUCT_SKU_CONFLICT);

        // Check exists sku(s)
        variantService.validateSkuNotExists(skus);

        // Handle product variant(s)
        List<ProductVariant> variants = r.getVariants().stream().map(req -> buildVariant(req, product)).toList();

        // Save all variants
        List<ProductVariant> savedVariants = variantRepository.saveAll(variants);
        Set<AttributeOption> allOptions = mapToAttributeOption(savedVariants);

        // Return product data. Include product, variant(s)
        return mapper.toResponse(productSaved, savedVariants, allOptions, new ProductMappingHelper());
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDetailsResponse getProductBySlug(String slug) {
        List<ProductVariant> variants = getVariants(slug);
        if (variants.isEmpty()) throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);

        // Because all variants are in the same product, get product at first variant.
        Product product = variants.getFirst().getProduct();

        Set<AttributeOption> allOptions = mapToAttributeOption(variants);

        return mapper.toResponse(product, variants, allOptions, new ProductMappingHelper());
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<ProductResponse> getSummaryProducts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<ProductResponse> products = repository.findAllSummary(pageable);

        return PageResponse.from(products);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<ProductResponse> getProducts(int pageNumber, int pageSize, String keyword, String sortBy) {
        // Handle sorting
        Sort sort = SortingUtils.sort(sortBy);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        String searchKey = (keyword == null) ? "" : keyword;

        Page<ProductResponse> products = repository.searchProducts(searchKey, pageable);

        return PageResponse.from(products);
    }

    public List<ProductVariant> getVariants(String slug) {
        return repository.findBySlugWithDetails(slug);
    }

    @Transactional
    @Override
    public ProductDetailsResponse updateProduct(Long id, ProductUpdateRequest r) {
        // Find a product by ID
        Product product = findById(id);

        // Validate slug. Slug must be different current slug and not already exists
        if (!r.getSlug().equals(product.getSlug()) && existsBySlug(r.getSlug()))
            throw new BusinessException(ErrorCode.PRODUCT_SLUG_CONFLICT);

        // If through the above validate. Update slug equals current slug. We not update it by set slug is null.
        r.setSlug(null);

        // Update product
        mapper.updateProduct(r, product);

        List<ProductVariant> currentVariants = variantService.getProductVariantsByProductId(id);

        Set<Long> requestIds = r.getVariants().stream()
                .map(ProductVariantUpdateRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        currentVariants.stream().filter(v -> !requestIds.contains(v.getId())).forEach(variantService::deleteVariant);

        List<ProductVariant> finalVariants = r.getVariants().stream().map(vReq -> {
            ProductVariant variant;

            // If id is null then update or insert it.
            if (vReq.getId() != null) {
                // Update case
                variant = variantService.findById(vReq.getId());
                variantService.updateVariant(variant, vReq);
            } else {
                // Insert case
                variant = variantMapper.toEntity(vReq);
                variant.setProduct(product);
            }

            // Update attribute options for owner variants
            Set<AttributeOption> options = optionService.getAttributeOptionsById(vReq.getAttributeOptionIds());
            variant.setOptions(options);

            return variant;
        }).toList();

        List<ProductVariant> savedVariants = variantService.saveAll(finalVariants);

        product.setMinPrice(PriceUtils.calcMinPrice(savedVariants));
        Product savedProduct = repository.save(product);

        Set<AttributeOption> allOptions = mapToAttributeOption(savedVariants);

        return mapper.toResponse(savedProduct, savedVariants, allOptions, new ProductMappingHelper());
    }

    public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private ProductVariant buildVariant(ProductVariantRequest r, Product product) {
        Set<AttributeOption> attributeOptions = optionService.getAttributeOptionsById(r.getAttributeOptionIds());

        ProductVariant variant = variantMapper.toEntity(r);
        variant.setProduct(product);
        variant.setOptions(attributeOptions);

        return variant;
    }

    private Set<AttributeOption> mapToAttributeOption(List<ProductVariant> variants) {
        return variants.stream().flatMap(variant -> variant.getOptions().stream()).collect(Collectors.toSet());
    }

    private boolean existsBySlug(String slug) {
        return repository.existsBySlug(slug);
    }
}
