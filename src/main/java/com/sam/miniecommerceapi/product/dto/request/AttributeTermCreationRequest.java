package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AttributeTermCreationRequest(
        @NotBlank(message = "product.validation.attribute_term_name_required")
        @Size(min = 1, message = "product.validation.attribute_term_name_size")
        String name
) {}
