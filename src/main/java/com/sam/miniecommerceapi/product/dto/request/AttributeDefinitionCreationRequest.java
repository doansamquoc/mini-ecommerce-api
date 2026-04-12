package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.common.constant.DataType;
import com.sam.miniecommerceapi.common.validator.ExitsId;
import com.sam.miniecommerceapi.product.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AttributeDefinitionCreationRequest(
	@NotNull(message = "category.id.required")
	@ExitsId(entity = Category.class, message = "category.not_found")
	Long categoryId,

	@NotBlank(message = "attribute.attribute_key.required")
	@Size(min = 2, max = 32, message = "attribute.attribute_key.size")
	String attributeKey,

	@NotBlank(message = "attribute.attribute_name.required")
	@Size(min = 2, max = 32, message = "attribute.attribute_name.size")
	String attributeName,

	@NotNull(message = "attribute.data_type.required")
	DataType dataType,
	boolean required
) {}
