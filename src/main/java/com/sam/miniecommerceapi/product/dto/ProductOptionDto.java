package com.sam.miniecommerceapi.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

public record ProductOptionDto(
	Long id,
	String name,
	List<String> values,

	@Min(value = 0, message = "product.option.position_minimum")
	@Max(value = 3, message = "product.option.position_maximum")
	Integer position
) {}
