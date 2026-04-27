package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {

	ProductResponse insert(ProductCreationRequest req);

	ProductResponse createProduct(ProductCreationRequest request);

	ProductResponse updateProduct(Long id, ProductUpdateRequest request);

	@Transactional(readOnly = true)
	ProductDetailsResponse getProductDetailsBySlug(String slug);

	@Transactional
	void deleteProduct(Long id);

	@Transactional
	void deleteAllProduct();

	Product findById(Long id);
}
