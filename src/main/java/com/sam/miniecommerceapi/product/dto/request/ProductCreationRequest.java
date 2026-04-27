package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.common.validator.ExitsId;
import com.sam.miniecommerceapi.product.dto.ProductOptionDto;
import com.sam.miniecommerceapi.product.entity.Category;
import com.sam.miniecommerceapi.upload.entity.Image;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record ProductCreationRequest(
	@NotBlank(message = "product.name.required")
	@Size(min = 5, max = 225, message = "product.name.size.regexp")
	String name,

	@Size(min = 15, message = "product.description.size.regexp")
	String description,

	@NotNull(message = "product.price.required")
	@Min(value = 1000, message = "product.price.min")
	BigDecimal regularPrice,

	@NotNull(message = "category.id.required")
	@ExitsId(entity = Category.class, message = "category.not_found")
	Long categoryId,

	@NotNull(message = "image.id.required")
	@ExitsId(entity = Image.class, message = "image.not_found")
	Long imageId,

	@Valid
	List<ProductOptionDto> options,

	@Valid
	List<VariantCreationRequest> variants
) {}
