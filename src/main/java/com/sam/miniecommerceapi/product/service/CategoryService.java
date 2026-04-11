package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.common.dto.response.PageResponse;
import com.sam.miniecommerceapi.product.dto.request.CategoryCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.CategoryUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.CategoryResponse;
import com.sam.miniecommerceapi.product.entity.Category;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
	PageResponse<CategoryResponse> getCategories(Pageable pageable);

	CategoryResponse getCategoryById(Long id);

	CategoryResponse getCategorySlug(String slug);

	CategoryResponse getCategoryByName(String name);

	CategoryResponse createCategory(CategoryCreationRequest r);

	CategoryResponse updateCategory(Long id, CategoryUpdateRequest r);

	Category getReference(Long id);

	Category findById(Long id);

	Category findByName(String name);

	Category findBySlug(String slug);

	boolean existedByName(String name);

	boolean existedBySlug(String slug);
}
