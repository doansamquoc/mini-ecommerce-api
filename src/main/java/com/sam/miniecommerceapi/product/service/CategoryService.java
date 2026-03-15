package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.common.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.product.dto.request.CategoryCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.CategoryUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.CategoryResponse;

public interface CategoryService {
    PageResponse<CategoryResponse> getCategories(int pageNumber, int pageSize);

    CategoryResponse createCategory(CategoryCreationRequest r);

    CategoryResponse updateCategory(String id, CategoryUpdateRequest r);
}
