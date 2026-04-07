package com.sam.miniecommerceapi.product.dto.response;

public record AttributeWithTermResponse(
        Long id, // Selected AttributeTerm.id
        String name, // Selected Attribute.name
        String option // Selected AttributeTerm.name
) {}
