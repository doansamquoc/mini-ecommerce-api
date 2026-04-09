package com.sam.miniecommerceapi.product.dto.response;

import com.sam.miniecommerceapi.common.constant.DataType;

public record AttributeDefinitionResponse(
	Long id,
	Long categoryId,
	String attributeKey,
	String attributeName,
	DataType dataType,
	boolean required
) {}
