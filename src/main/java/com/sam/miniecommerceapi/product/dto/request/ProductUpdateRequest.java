package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public record ProductUpdateRequest(
	@Size(min = 2, max = 255, message = "product.validation.name_size")
	String name,

	@Size(min = 2, message = "product.validation.description_size")
	String description,

	@Min(value = 1000, message = "product.validation.price_min")
	BigDecimal regularPrice,
	Long categoryId,

	@URL(message = "product.validation.image_invalid_url")
	String imageUrl
) {}
