package com.sam.miniecommerceapi.product.dto.response;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public record ProductDetailsResponse(
	Long id,
	String name,
	String slug,
	String description,
	BigDecimal price,
	String imageUrl,
	CategoryResponse category,
	Set<VariantResponse> variants,
	Map<String, Set<Object>> attributes
) {}
