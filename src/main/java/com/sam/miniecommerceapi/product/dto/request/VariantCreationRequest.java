package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.Map;

public record VariantCreationRequest(
	@NotBlank(message = "product.sku.required")
	@Size(min = 2, max = 16, message = "product.sku.size")
	String sku,

	@NotNull(message = "product.price.required")
	@Min(value = 1000, message = "product.price.min")
	BigDecimal price,

	@NotNull(message = "product.stock.required")
	@PositiveOrZero(message = "product.stock.min")
	Integer stockQuantity,

	@NotNull(message = "product.image_id.required")
	Long imageId,
	Map<String, Object> attributes
) {}
