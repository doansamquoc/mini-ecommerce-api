package com.sam.miniecommerceapi.product.util;

import com.sam.miniecommerceapi.product.dto.request.ProductVariantRequest;
import com.sam.miniecommerceapi.product.entity.ProductVariant;

import java.math.BigDecimal;
import java.util.List;

public class PriceUtils {
    public static BigDecimal calcMinPrice(List<ProductVariant> variants) {
        return variants.stream().map(ProductVariant::getPrice).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    public static BigDecimal calcMinPriceByRequest(List<ProductVariantRequest> variantRequests) {
        return variantRequests.stream()
                .map(ProductVariantRequest::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}
