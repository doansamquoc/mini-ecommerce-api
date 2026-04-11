package com.sam.miniecommerceapi.product.dto.response;

import java.math.BigDecimal;

public record ProductResponse(
	Long id,
	String name,
	BigDecimal price,
	String slug,
	String imageUrl,
	CategoryResponse category
) {}
