package com.sam.miniecommerceapi.product.dto.response;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal regularPrice,
        String slug,
        String imageUrl,
        CategoryResponse category
) {}
