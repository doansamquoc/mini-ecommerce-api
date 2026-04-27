package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.common.validator.ExitsId;
import com.sam.miniecommerceapi.product.entity.Category;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record VariantCreationRequest(
	@NotBlank(message = "product.sku.required")
	@Size(min = 2, max = 16, message = "product.sku.size.regexp")
	String sku,

	@NotNull(message = "product.price.required")
	@Min(value = 1000, message = "product.price.min")
	BigDecimal price,

	@NotNull(message = "product.stock.required")
	@PositiveOrZero(message = "product.stock.min")
	Integer stock,

	@NotNull(message = "image.id.required")
	@ExitsId(entity = Category.class, message = "image.not_found")
	Long imageId,
	String option1,
	String option2,
	String option3
) {}
