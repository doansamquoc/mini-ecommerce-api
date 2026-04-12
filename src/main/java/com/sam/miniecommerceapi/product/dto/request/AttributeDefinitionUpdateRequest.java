package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.common.constant.DataType;
import com.sam.miniecommerceapi.common.validator.ExitsId;
import com.sam.miniecommerceapi.product.entity.Category;
import jakarta.validation.constraints.Size;

public record AttributeDefinitionUpdateRequest(
	@ExitsId(entity = Category.class, message = "category.not_found")
	Long categoryId,

	@Size(min = 2, max = 32, message = "attribute.attribute_key.size")
	String attributeKey,

	@Size(min = 2, max = 32, message = "attribute.attribute_name.size")
	String attributeName,
	DataType dataType,
	boolean required
) {
}
