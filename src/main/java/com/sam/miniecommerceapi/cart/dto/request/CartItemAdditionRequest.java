package com.sam.miniecommerceapi.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemAdditionRequest(
	@NotNull(message = "cart.product_id.required")
	Long variantId,

	@NotNull(message = "cart.quantity.required")
	@Min(value = 1, message = "cart.quantity.min")
	int quantity
) {}
