package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.common.validator.ExitsId;
import com.sam.miniecommerceapi.product.entity.Category;
import com.sam.miniecommerceapi.upload.entity.Image;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductCreationRequest(
	@NotBlank(message = "product.validation.name_required")
	@Size(min = 2, max = 225, message = "product.validation.name_size")
	String name,

	@Size(min = 2, message = "product.validation.description_size")
	String description,

	@NotNull(message = "product.validation.price_required")
	@Min(value = 1000, message = "product.validation.price_min")
	BigDecimal price,

	@NotNull(message = "category.validation.id_required")
	@ExitsId(entity = Category.class, message = "category.not_found")
	Long categoryId,

	@NotNull(message = "product.image_id.required")
	@ExitsId(entity = Image.class, message = "image.not_found")
	Long imageId
) {}
