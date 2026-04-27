package com.sam.miniecommerceapi.product.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public record VariantResponse(
	String title,
	String sku,
	BigDecimal price,
	Integer stock,
	String src,
	String option1,
	String option2,
	String option3
) {}
