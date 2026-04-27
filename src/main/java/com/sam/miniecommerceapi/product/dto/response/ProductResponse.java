package com.sam.miniecommerceapi.product.dto.response;

import java.math.BigDecimal;

public record ProductResponse(
	String name,
	String description,
	BigDecimal regularPrice,
	String slug,
	String src,
	String categoryName
) {}
