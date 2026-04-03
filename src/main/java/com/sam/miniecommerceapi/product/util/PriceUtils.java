package com.sam.miniecommerceapi.product.util;

import com.sam.miniecommerceapi.product.dto.request.VariantRequest;
import com.sam.miniecommerceapi.product.entity.Variant;

import java.math.BigDecimal;
import java.util.List;

public class PriceUtils {
    public static BigDecimal calcMinPrice(List<Variant> variants) {
        return variants.stream().map(Variant::getPrice).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    public static BigDecimal calcMinPriceByRequest(List<VariantRequest> variantRequests) {
        return variantRequests.stream()
                .map(VariantRequest::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}
