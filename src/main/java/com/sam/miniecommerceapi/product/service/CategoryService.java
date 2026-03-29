package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.shared.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.product.dto.request.CategoryCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.CategoryUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.CategoryResponse;
import com.sam.miniecommerceapi.product.entity.Category;

public interface CategoryService {
    PageResponse<CategoryResponse> getCategories(int pageNumber, int pageSize);

    CategoryResponse getCategoryById(String id);

    CategoryResponse getCategorySlug(String slug);

    CategoryResponse getCategoryByName(String name);

    CategoryResponse createCategory(CategoryCreationRequest r);

    CategoryResponse updateCategory(String id, CategoryUpdateRequest r);

    Category findById(String id);

    Category findByName(String name);

    Category findBySlug(String slug);

    boolean existedByName(String name);

    boolean existedBySlug(String slug);
}
