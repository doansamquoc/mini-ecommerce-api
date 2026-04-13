package com.sam.miniecommerceapi.product.dto.response;

import java.math.BigDecimal;

public record ProductSearchResponse(
	String name,
	String slug,
	BigDecimal regularPrice,
	String imageUrl
) {}
