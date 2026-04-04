package com.sam.miniecommerceapi.product.service.impl;

import com.github.slugify.Slugify;
import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.AttributeValue;
import com.sam.miniecommerceapi.product.entity.Category;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.Variant;
import com.sam.miniecommerceapi.product.mapper.ProductMapper;
import com.sam.miniecommerceapi.product.mapper.VariantMapper;
import com.sam.miniecommerceapi.product.repository.ProductRepository;
import com.sam.miniecommerceapi.product.service.AttributeValueService;
import com.sam.miniecommerceapi.product.service.CategoryService;
import com.sam.miniecommerceapi.product.service.ProductService;
import com.sam.miniecommerceapi.product.service.VariantService;
import com.sam.miniecommerceapi.product.util.PriceUtils;
import com.sam.miniecommerceapi.product.util.SortingUtils;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import com.sam.miniecommerceapi.shared.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.shared.exception.BusinessException;
import com.sam.miniecommerceapi.shared.util.UUIDUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    Slugify slugify;
    ProductMapper mapper;
    ProductRepository repository;
    CategoryService categoryService;
    VariantMapper variantMapper;
    VariantService variantService;
    AttributeValueService optionService;

    @Override
    @Transactional
    public ProductDetailsResponse createProduct(ProductCreationRequest request) {
        log.info("Creating new product: {}", request.getName());
        validateProductRequest(request);

        // 2. Load Dependencies (Batch Loading)
        Category category = categoryService.findById(request.getCategoryId());
        Map<Long, AttributeValue> attributeValues = loadAttributeValues(request);

        // 3. Assemble Entity
        Product product = assembleProduct(request, category, attributeValues);

        // 4. Persistence
        Product savedProduct = repository.save(product);
        log.info("Product created successfully with ID: {} and Slug: {}", savedProduct.getId(), savedProduct.getSlug());

        // 5. Response Mapping
        return mapper.toDetailsResponse(savedProduct);
    }

    private void validateProductRequest(ProductCreationRequest request) {
        // Validate internal SKU uniqueness in the request
        List<String> skus = request.getVariants().stream().map(VariantRequest::getSku).toList();
        if (skus.size() != new HashSet<>(skus).size()) {
            throw new BusinessException(ErrorCode.PRODUCT_SKU_CONFLICT);
        }

        // Validate SKU existence in the database
        variantService.validateSkuNotExists(skus);
    }

    private Map<Long, AttributeValue> loadAttributeValues(ProductCreationRequest request) {
        Set<Long> allAttributeValueIds = request.getVariants().stream()
                .flatMap(v -> v.getAttributeValueIds().stream())
                .collect(Collectors.toSet());

        return optionService.findAllById(allAttributeValueIds).stream()
                .collect(Collectors.toMap(AttributeValue::getId, av -> av));
    }

    private Product assembleProduct(ProductCreationRequest request, Category category, Map<Long, AttributeValue> attrMap) {
        Product product = mapper.toProduct(request);

        // Enrich Product properties
        product.setCategory(category);
        product.setSlug(makeUniqueSlug(product.getName()));
        product.setMinPrice(PriceUtils.calcMinPriceByRequest(request.getVariants()));

        // Wire up Variants and their Attribute Values
        product.getVariants().forEach(variant -> {
            variant.setProduct(product);

            // Match back to request to get attribute IDs for this specific variant
            VariantRequest vReq = request.getVariants().stream()
                    .filter(r -> r.getSku().equals(variant.getSku()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(ErrorCode.SERVER_INTERNAL));

            Set<AttributeValue> fullValues = vReq.getAttributeValueIds().stream()
                    .map(attrMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            variant.setValues(fullValues);
        });

        return product;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailsResponse getProductBySlug(String slug) {
        Product product = findBySlug(slug);
        return mapper.toDetailsResponse(product);
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

        List<Variant> currentVariants = variantService.getProductVariantsByProductId(id);

        Set<Long> requestIds = r.getVariants().stream()
                .map(VariantUpdateRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        currentVariants.stream().filter(v -> !requestIds.contains(v.getId())).forEach(variantService::deleteVariant);

        List<Variant> finalVariants = r.getVariants().stream().map(vReq -> {
            Variant variant;

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
            Set<AttributeValue> options = optionService.findAllById(vReq.getAttributeOptionIds());
            variant.setValues(options);

            return variant;
        }).toList();

        List<Variant> savedVariants = variantService.saveAll(finalVariants);

        product.setMinPrice(PriceUtils.calcMinPrice(savedVariants));
        Product savedProduct = repository.save(product);

        return mapper.toDetailsResponse(savedProduct);
    }

    public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private Set<AttributeValue> mapToAttributeOption(List<Variant> variants) {
        return variants.stream().flatMap(variant -> variant.getValues().stream()).collect(Collectors.toSet());
    }

    private boolean existsBySlug(String slug) {
        return repository.existsBySlug(slug);
    }

    private Product findBySlug(String slug) {
        return repository.findBySlug(slug).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private String makeUniqueSlug(String input) {
        return slugify.slugify(input) + "-" + UUIDUtils.generateSlugSuffix();
    }

    private void validateSkus(List<VariantRequest> requests) {
        List<String> skus = requests.stream().map(VariantRequest::getSku).toList();
        if (skus.size() != new HashSet<>(skus).size()) throw new BusinessException(ErrorCode.PRODUCT_SKU_CONFLICT);
        variantService.validateSkuNotExists(skus);
    }
}
