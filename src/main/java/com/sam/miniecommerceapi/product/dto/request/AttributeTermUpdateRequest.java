package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.Size;

public record AttributeTermUpdateRequest(
        @Size(min = 1, message = "product.validaton.attribute_term_name_size")
        String name
) {}
