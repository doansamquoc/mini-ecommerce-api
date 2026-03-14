package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.request.CategoryCreationRequest;
import com.sam.miniecommerceapi.product.dto.response.CategoryResponse;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreationRequest r);
}
