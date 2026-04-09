package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.request.VariantCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantRequest;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.entity.Variant;

import java.util.List;

public interface VariantService {

	VariantResponse update(Long productId, Long variantId, VariantRequest request);

	VariantResponse create(Long productId, VariantCreationRequest request);

	VariantResponse read(Long productId, Long variantId);

	List<VariantResponse> readAll(Long productId);

	void delete(Long productId, Long variantId);

	void deleteAll(Long productId);

	int deductStock(Long id, int quantity);

	void increaseStock(Long id, int quantity);

	Variant findById(Long variantId);

	Variant findGraphById(Long id);
}
