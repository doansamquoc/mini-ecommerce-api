package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.request.VariantCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.ProductVariant;

import java.util.List;

public interface ProductVariantService {

	List<ProductVariant> mapVariants(List<VariantCreationRequest> reqs);

	VariantResponse updateVariant(Long productId, Long variantId, VariantUpdateRequest request);

	VariantResponse createVariant(Long productId, VariantCreationRequest request);

	VariantResponse getVariant(Long productId, Long variantId);

	List<VariantResponse> getAllVariants(Long productId);

	void delete(Long productId, Long variantId);

	void deleteAll(Long productId);

	int deductStock(Long id, int quantity);

	void increaseStock(Long id, int quantity);

	ProductVariant findById(Long variantId);

	ProductVariant findGraphById(Long id);
}
