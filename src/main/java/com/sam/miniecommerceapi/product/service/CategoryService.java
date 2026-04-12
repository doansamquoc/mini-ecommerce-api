package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.request.CategoryCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.CategoryUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.CategoryResponse;
import com.sam.miniecommerceapi.product.entity.Category;

import java.util.List;

public interface CategoryService {
	List<CategoryResponse> getAllCategories();

	CategoryResponse getCategoryById(Long id);

	CategoryResponse getCategorySlug(String slug);

	CategoryResponse getCategoryByName(String name);

	CategoryResponse createCategory(CategoryCreationRequest r);

	CategoryResponse updateCategory(Long id, CategoryUpdateRequest r);

	Category getReference(Long id);

	Category findById(Long id);

	Category findByName(String name);

	Category findBySlug(String slug);

	boolean existsByName(String name);

	boolean existsBySlug(String slug);
}
