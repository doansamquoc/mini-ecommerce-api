package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.common.validator.ExitsId;
import com.sam.miniecommerceapi.product.entity.Category;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.Map;

public record VariantCreationRequest(
	@NotBlank(message = "product.sku.required")
	@Size(min = 2, max = 16, message = "product.sku.size")
	String sku,

	@NotNull(message = "product.regularPrice.required")
	@Min(value = 1000, message = "product.regularPrice.min")
	BigDecimal price,

	@NotNull(message = "product.stock.required")
	@PositiveOrZero(message = "product.stock.min")
	Integer stockQuantity,

	@NotNull(message = "product.image_id.required")
	@ExitsId(entity = Category.class, message = "image.not_found")
	Long imageId,
	Map<String, Object> attributes
) {}
