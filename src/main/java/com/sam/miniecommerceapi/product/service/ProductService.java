package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {
    @Transactional
    ProductResponse createProduct(ProductCreationRequest r);

    @Transactional(readOnly = true)
    ProductResponse getProductBySlug(String slug);
}
