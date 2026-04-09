package com.sam.miniecommerceapi.cart.dto.request;

import jakarta.validation.constraints.NotNull;

public record CartCreationRequest(
	@NotNull(message = "cart.user_id.required")
	Long userId
) {}
