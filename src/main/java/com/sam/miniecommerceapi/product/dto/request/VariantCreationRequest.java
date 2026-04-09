package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.Map;

public record VariantCreationRequest(
	@NotBlank(message = "product.validation.sku_required")
	@Size(min = 2, max = 16, message = "product.validation.sku_size")
	String sku,

	@NotNull(message = "product.validation.price_required")
	@Min(value = 1000, message = "product.validation.price_min")
	BigDecimal price,

	@NotNull(message = "product.validation.stock_required")
	@PositiveOrZero(message = "product.validation.stock_min")
	Integer stockQuantity,

	@NotBlank(message = "product.validation.image_required")
	@URL(message = "product.validation.image_invalid_url")
	String imageUrl,
	Map<String, Object> attributes
) {}
