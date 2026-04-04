package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.shared.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {
    @Transactional
    ProductDetailsResponse createProduct(ProductCreationRequest r);

    @Transactional(readOnly = true)
    ProductDetailsResponse getProductBySlug(String slug);

    @Transactional
    ProductDetailsResponse updateProduct(Long id, ProductUpdateRequest r);
}
