package com.sam.miniecommerceapi.cart.dto.request;

import jakarta.validation.constraints.NotNull;

public record CartItemUpdateRequest(
        @NotNull(message = "message.validation.id_required")
        Long variantId,

        @NotNull(message = "message.validation.quantity_required")
        int quantity
) {}
