package com.sam.miniecommerceapi.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
	@NotNull(message = "order.variant_id.required")
	Long variantId,
	@Min(value = 1, message = "order.quantity.min")
	Integer quantity
) {}
