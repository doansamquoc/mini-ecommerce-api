package com.sam.miniecommerceapi.cart.dto.request;

import jakarta.validation.constraints.NotNull;

public record CartItemUpdateRequest(
	@NotNull(message = "cart.product_id.required")
	Long variantId,

	@NotNull(message = "cart.quantity.required")
	int quantity
) {}
