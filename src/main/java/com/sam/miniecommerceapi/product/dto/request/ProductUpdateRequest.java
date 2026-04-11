package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public record ProductUpdateRequest(
	@Size(min = 2, max = 255, message = "product.name.size")
	String name,

	@Size(min = 15, message = "product.description.size")
	String description,

	@Min(value = 1000, message = "product.price.min")
	BigDecimal price,
	Long categoryId,
	Long imageId
) {}
