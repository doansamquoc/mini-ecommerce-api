package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.shared.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import com.sam.miniecommerceapi.shared.exception.BusinessException;
import com.sam.miniecommerceapi.product.dto.request.CategoryCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.CategoryUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.CategoryResponse;
import com.sam.miniecommerceapi.product.entity.Category;
import com.sam.miniecommerceapi.product.mapper.CategoryMapper;
import com.sam.miniecommerceapi.product.repository.CategoryRepository;
import com.sam.miniecommerceapi.product.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository repository;
    CategoryMapper mapper;

    /**
     * @param pageNumber page number
     * @param pageSize   page size
     * @return List of category
     */
    @Override
    public PageResponse<CategoryResponse> getCategories(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Category> categories = repository.findAll(pageable);
        Page<CategoryResponse> responses = categories.map(mapper::toResponse);

        return PageResponse.from(responses);
    }

    /**
     * Get category by ID
     *
     * @param id Category ID
     * @return Category data
     */
    @Override
    public CategoryResponse getCategoryById(String id) {
        return mapper.toResponse(findById(id));
    }

    /**
     * Get category by slug
     *
     * @param slug Category slug
     * @return Category data
     */
    @Override
    public CategoryResponse getCategorySlug(String slug) {
        return mapper.toResponse(findBySlug(slug));
    }

    /**
     * Get category by name
     *
     * @param name Category name
     * @return Category data
     */
    @Override
    public CategoryResponse getCategoryByName(String name) {
        return mapper.toResponse(findByName(name));
    }

    @Override
    public CategoryResponse createCategory(CategoryCreationRequest r) {
        if (existedByName(r.getName()))
            throw new BusinessException(ErrorCode.CATEGORY_NAME_CONFLICT);
        if (existedBySlug(r.getSlug()))
            throw new BusinessException(ErrorCode.CATEGORY_SLUG_CONFLICT);

        Category category = mapper.toCategory(r);

        return mapper.toResponse(repository.save(category));
    }

    /**
     * Update category information by ID
     *
     * @param id Category ID
     * @param r  Category update request
     * @return Category updated
     */
    @Override
    public CategoryResponse updateCategory(String id, CategoryUpdateRequest r) {
        if (!r.getName().isBlank() && existedByName(r.getName()))
            throw new BusinessException(ErrorCode.CATEGORY_NAME_CONFLICT);
        if (!r.getSlug().isBlank() && existedBySlug(r.getSlug()))
            throw new BusinessException(ErrorCode.CATEGORY_SLUG_CONFLICT);

        Category category = findById(id);
        category = mapper.toCategory(r, category);

        return mapper.toResponse(repository.save(category));
    }

    @Override
    public Category findById(String id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public Category findByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public Category findBySlug(String slug) {
        return repository.findBySlug(slug).orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public boolean existedByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public boolean existedBySlug(String slug) {
        return repository.existsBySlug(slug);
    }
}
