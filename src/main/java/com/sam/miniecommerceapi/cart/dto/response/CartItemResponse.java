package com.sam.miniecommerceapi.cart.dto.response;

import java.math.BigDecimal;

public record CartItemResponse(
	Long id,
	Long variantId,
	String productName,
	String sku,
	String src,
	BigDecimal price,
	int quantity,
	BigDecimal subTotal
) {}
