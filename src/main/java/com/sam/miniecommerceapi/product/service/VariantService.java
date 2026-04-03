package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.request.VariantUpdateRequest;
import com.sam.miniecommerceapi.product.entity.Variant;

import java.util.List;

public interface VariantService {
    boolean existsBySku(String sku);

    void validateSkuNotExists(List<String> skus);

    List<Variant> getProductVariantsByProductId(Long id);

    int deductStock(Long id, int quantity);

    void increaseStock(Long id, int quantity);

    void updateVariant(Variant variant, VariantUpdateRequest r);

    Variant findById(Long id);

    void deleteVariant(Variant variant);

    List<Variant> saveAll(List<Variant> finalVariants);

    Variant save(Variant variant);

    boolean existsById(Long id);

    List<Variant> findAllByIds(List<Long> productVariantIds);
}
