package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AttributeCreationRequest(
        @NotBlank(message = "product.validation.attribute_name_required")
        @Size(min = 2, message = "product.validation.attribute_name_size")
        String name
) {}