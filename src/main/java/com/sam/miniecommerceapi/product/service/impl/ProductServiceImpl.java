package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.common.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.product.dto.request.ProductVariantRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
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
        Sort sort = handleSorting(sortBy);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        String searchKey = (keyword == null) ? "" : keyword;

        Page<ProductResponse> products = repository.searchProducts(searchKey, pageable);

        return PageResponse.from(products);
    }

    public List<ProductVariant> getVariants(String slug) {
        return repository.findBySlugWithDetails(slug);
    }

    private Sort handleSorting(String sortBy) {
        return switch (sortBy) {
            case "price-desc" -> Sort.by("minPrice").descending();
            case "price-asc" -> Sort.by("minPrice").ascending();
            case "newest" -> Sort.by("createdAt").descending();
            default -> Sort.by("name").ascending();
        };
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
