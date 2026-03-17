package com.sam.miniecommerceapi.product.service;

import java.util.List;

public interface ProductVariantService {
    boolean existsBySku(String sku);

    void validateSkuNotExists(List<String> skus);
}
