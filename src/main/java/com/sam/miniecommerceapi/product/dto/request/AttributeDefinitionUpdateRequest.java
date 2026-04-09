package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.common.constant.DataType;
import jakarta.validation.constraints.Size;

public record AttributeDefinitionUpdateRequest(
	Long categoryId,
	@Size(min = 2, max = 32, message = "message.validation.key_size")
	String attributeKey,

	@Size(min = 2, max = 32, message = "message.validation.name_size")
	String attributeName,
	DataType dataType,
	boolean required
) {
}
