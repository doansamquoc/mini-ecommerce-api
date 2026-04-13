package com.sam.miniecommerceapi.product.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public record VariantResponse(
	String sku,
	BigDecimal price,
	Integer stockQuantity,
	String imageUrl,
	Map<String, Object> attributes
) {}
