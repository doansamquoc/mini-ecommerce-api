package com.sam.miniecommerceapi.product.dto.response;

import java.math.BigDecimal;

public record ProductResponse(
	Long id,
	String name,
	String description,
	BigDecimal regularPrice,
	String slug,
	String imageUrl,
	String categoryName
) {}
