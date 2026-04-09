package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public record ProductCreationRequest(
	@NotBlank(message = "product.validation.name_required")
	@Size(min = 2, max = 225, message = "product.validation.name_size")
	String name,

	@Size(min = 2, message = "product.validation.description_size")
	String description,

	@NotNull(message = "product.validation.price_required")
	@Min(value = 1000, message = "product.validation.price_min")
	BigDecimal regularPrice,

	@NotNull(message = "category.validation.id_required")
	Long categoryId,

	@NotBlank(message = "product.validation.image_required")
	@URL(message = "product.validation.image_invalid_url")
	String imageUrl
) {}
