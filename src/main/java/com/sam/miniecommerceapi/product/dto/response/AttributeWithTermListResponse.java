package com.sam.miniecommerceapi.product.dto.response;

import com.sam.miniecommerceapi.product.entity.AttributeTerm;

import java.util.List;

public record AttributeWithTermListResponse(
        Long id,
        String name,
        List<AttributeTermResponse> values
) {}
