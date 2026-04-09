package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.request.AttributeDefinitionCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.AttributeDefinitionUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeDefinitionResponse;

import java.util.List;
import java.util.Map;

public interface AttributeDefinitionService {
	AttributeDefinitionResponse createAttribute(AttributeDefinitionCreationRequest request);

	AttributeDefinitionResponse getById(Long id);

	AttributeDefinitionResponse getByAttributeKey(String attributeKey);

	List<AttributeDefinitionResponse> getAllByCategoryId(Long categoryId);

	List<AttributeDefinitionResponse> getAllAttributes();

	AttributeDefinitionResponse updateAttribute(Long id, AttributeDefinitionUpdateRequest request);

	void deleteAll();

	void deleteByCategoryId(Long categoryId);

	void deleteById(Long id);

	void attributeNormalizer(Long categoryId, Map<String, Object> attributes);
}
