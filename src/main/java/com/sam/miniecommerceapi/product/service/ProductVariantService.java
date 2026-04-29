package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.request.VariantCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductVariantRequest;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.ProductVariant;

import java.util.List;
import java.util.Set;

public interface ProductVariantService {

	List<ProductVariant> mapVariants(List<VariantCreationRequest> reqs);

	VariantResponse update(Long productId, Long variantId, ProductVariantRequest request);

	ProductVariant mapUpdate(ProductVariantRequest req, ProductVariant variant);

	ProductVariant mapEntity(ProductVariantRequest req);

	void batchUpdate(Product product, List<ProductVariantRequest> reqs);

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
