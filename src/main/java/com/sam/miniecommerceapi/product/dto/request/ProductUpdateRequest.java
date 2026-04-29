package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.product.dto.ProductOptionDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record ProductUpdateRequest(
	@Size(min = 2, max = 255, message = "product.name.size.regexp")
	String name,

	@Size(min = 15, message = "product.description.size.regexp")
	String description,

	@Min(value = 1000, message = "product.regularPrice.min")
	BigDecimal price,
	Long categoryId,
	Long imageId,

	@Valid
	List<ProductVariantRequest> variants,

	@Valid
	@Size(max = 3, message = "product.option.maximum")
	List<ProductOptionDto> options
) {}
