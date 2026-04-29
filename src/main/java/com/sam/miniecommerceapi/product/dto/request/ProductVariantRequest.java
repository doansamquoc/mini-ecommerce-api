package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.common.validator.ExitsId;
import com.sam.miniecommerceapi.product.entity.Category;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;

public record ProductVariantRequest(
	Long id,

	@NotBlank(message = "product.slug.required")
	@Size(min = 2, max = 16, message = "product.slug.size")
	String sku,

	@NotNull(message = "product.regularPrice.required")
	@Min(value = 1000, message = "product.regularPrice.min")
	BigDecimal price,

	@NotNull(message = "product.stock.required")
	@PositiveOrZero(message = "product.stock.min")
	Integer stock,

	@NotNull(message = "product.iamge_id.required")
	@ExitsId(entity = Category.class, message = "image.not_found")
	Long imageId,

	String option1,
	String option2,
	String option3
) {

}
