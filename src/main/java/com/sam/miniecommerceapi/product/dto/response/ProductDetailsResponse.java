package com.sam.miniecommerceapi.product.dto.response;

import com.sam.miniecommerceapi.product.dto.ProductOptionDto;

import java.math.BigDecimal;
import java.util.Set;

public record ProductDetailsResponse(
	Long id,
	String name,
	String slug,
	String description,
	BigDecimal regularPrice,
	String src,
	CategoryResponse category,
	Set<VariantResponse> variants,
	Set<ProductOptionDto> options
) {}
