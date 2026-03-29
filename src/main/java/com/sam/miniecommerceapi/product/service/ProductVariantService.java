package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.request.ProductVariantUpdateRequest;
import com.sam.miniecommerceapi.product.entity.ProductVariant;

import java.util.List;

public interface ProductVariantService {
    boolean existsBySku(String sku);

    void validateSkuNotExists(List<String> skus);

    List<ProductVariant> getProductVariantsByProductId(Long id);

    int deductStock(Long id, int quantity);

    void increaseStock(Long id, int quantity);

    void updateVariant(ProductVariant variant, ProductVariantUpdateRequest r);

    ProductVariant findById(Long id);

    void deleteVariant(ProductVariant variant);

    List<ProductVariant> saveAll(List<ProductVariant> finalVariants);

    ProductVariant save(ProductVariant variant);

    boolean existsById(Long id);

    List<ProductVariant> findAllByIds(List<Long> productVariantIds);
}
