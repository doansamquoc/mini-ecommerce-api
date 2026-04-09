package com.sam.miniecommerceapi.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemAdditionRequest(
        @NotNull(message = "message.validation.id_required")
        Long variantId,

        @NotNull(message = "message.validation.quantity_required")
        @Min(value = 1, message = "message.validation.quantity_min")
        int quantity
) {}
