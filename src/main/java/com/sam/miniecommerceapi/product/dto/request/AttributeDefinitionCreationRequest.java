package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.common.constant.DataType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AttributeDefinitionCreationRequest(
	@NotNull(message = "message.validation.id_required")
	Long categoryId,

	@NotBlank(message = "message.validation.key_required")
	@Size(min = 2, max = 32, message = "messave.validation.key_size")
	String attributeKey,

	@NotBlank(message = "message.validation.name_required")
	@Size(min = 2, max = 32, message = "message.validation.name_size")
	String attributeName,

	@NotNull(message = "message.validation.type_invalid")
	DataType dataType,
	boolean required
) {}
