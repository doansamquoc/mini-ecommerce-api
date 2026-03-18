package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.product.dto.request.ProductVariantUpdateRequest;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.product.mapper.ProductVariantMapper;
import com.sam.miniecommerceapi.product.repository.ProductVariantRepository;
import com.sam.miniecommerceapi.product.service.ProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantServiceImpl implements ProductVariantService {
    ProductVariantMapper mapper;
    ProductVariantRepository repository;

    @Override
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }

    /**
     * Purpose: Avoid N + 1 query by check all of sku in once query.<br/>
     * Check sku already exists. If existed throw an exception with that sku(s).
     *
     * @param skus List of sku (String)
     */
    @Override
    public void validateSkuNotExists(List<String> skus) {
        List<String> existedSkus = repository.findExistingSkus(skus);
        if (!existedSkus.isEmpty()) {
            throw new BusinessException(ErrorCode.PRODUCT_SKU_ALREADY_EXISTS, Map.of("skus", existedSkus));
        }
    }

    /**
     * Get all variants by product ID
     *
     * @param id Product ID
     * @return List variants by product ID
     */
    @Override
    public List<ProductVariant> getProductVariantsByProductId(Long id) {
        return repository.findAllByProductId(id);
    }


    @Override
    public void updateVariant(ProductVariant variant, ProductVariantUpdateRequest r) {
        ProductVariant newVariant = mapper.toEntity(r, variant);
        repository.save(newVariant);
    }

    @Override
    public ProductVariant findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
    }

    @Override
    public void deleteVariant(ProductVariant variant) {
        repository.delete(variant);
    }

    @Override
    public List<ProductVariant> saveAll(List<ProductVariant> variants) {
        return repository.saveAll(variants);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
