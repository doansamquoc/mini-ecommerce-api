package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.product.dto.request.CategoryCreationRequest;
import com.sam.miniecommerceapi.product.dto.response.CategoryResponse;
import com.sam.miniecommerceapi.product.entity.Category;
import com.sam.miniecommerceapi.product.mapper.CategoryMapper;
import com.sam.miniecommerceapi.product.repository.CategoryRepository;
import com.sam.miniecommerceapi.product.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository repository;
    CategoryMapper mapper;

    @Override
    public CategoryResponse createCategory(CategoryCreationRequest r) {
        if (existedByName(r.getName())) throw new BusinessException(ErrorCode.PRODUCT_NAME_CONFLICT);
        if (existedBySlug(r.getSlug())) throw new BusinessException(ErrorCode.PRODUCT_SLUG_CONFLICT);

        Category category = mapper.toCategory(r);

        return mapper.toResponse(repository.save(category));
    }


    public Category findById(String id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public Category findByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public Category findBySlug(String slug) {
        return repository.findBySlug(slug).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public boolean existedByName(String name) {
        return repository.existsByName(name);
    }

    public boolean existedBySlug(String slug) {
        return repository.existsBySlug(slug);
    }
}
