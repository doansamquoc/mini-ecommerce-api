package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.common.validator.ExitsId;
import com.sam.miniecommerceapi.product.entity.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductUpdateRequest(
	@Size(min = 2, max = 255, message = "product.name.size")
	String name,

	@Size(min = 15, message = "product.description.size")
	String description,

	@Min(value = 1000, message = "product.regularPrice.min")
	BigDecimal price,

	@ExitsId(entity = Category.class, message = "category.not_found")
	Long categoryId,

	@ExitsId(entity = Category.class, message = "image.not_found")
	Long imageId
) {}
