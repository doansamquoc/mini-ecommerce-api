package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.dto.response.PageResponse;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.product.dto.request.CategoryCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.CategoryUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.CategoryResponse;
import com.sam.miniecommerceapi.product.entity.Category;
import com.sam.miniecommerceapi.product.mapper.CategoryMapper;
import com.sam.miniecommerceapi.product.repository.CategoryRepository;
import com.sam.miniecommerceapi.product.service.CategoryService;
import com.sam.miniecommerceapi.upload.entity.Image;
import com.sam.miniecommerceapi.upload.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
	CategoryMapper mapper;
	CategoryRepository repository;
	private final ImageService imageService;

	@Override
	public PageResponse<CategoryResponse> getCategories(Pageable pageable) {
		Page<Category> categories = repository.findAll(pageable);
		Page<CategoryResponse> responses = categories.map(mapper::toResponse);
		return PageResponse.from(responses);
	}

	@Override
	public CategoryResponse getCategoryById(Long id) {
		return mapper.toResponse(findById(id));
	}

	@Override
	public CategoryResponse getCategorySlug(String slug) {
		return mapper.toResponse(findBySlug(slug));
	}

	@Override
	public CategoryResponse getCategoryByName(String name) {
		return mapper.toResponse(findByName(name));
	}

	@Override
	public CategoryResponse createCategory(CategoryCreationRequest req) {
		if (existedByName(req.getName())) throw BusinessException.of(ErrorCode.CATEGORY_NAME_CONFLICT);
		if (existedBySlug(req.getSlug())) throw BusinessException.of(ErrorCode.CATEGORY_SLUG_CONFLICT);

		Image image = imageService.findById(req.getImageId());
		Category category = mapper.toEntity(req);
		category.setImage(image);

		return mapper.toResponse(repository.save(category));
	}

	@Override
	public CategoryResponse updateCategory(Long id, CategoryUpdateRequest req) {
		if (!req.getName().isBlank() && existedByName(req.getName())) {
			throw BusinessException.of(ErrorCode.CATEGORY_NAME_CONFLICT);
		}
		if (!req.getSlug().isBlank() && existedBySlug(req.getSlug())) {
			throw BusinessException.of(ErrorCode.CATEGORY_SLUG_CONFLICT);
		}

		Category category = findById(id);
		mapper.toEntity(req, category);
		Image image = imageService.findById(req.getImageId());
		category.setImage(image);

		return mapper.toResponse(repository.save(category));
	}

	@Override
	public Category getReference(Long id) {
		return repository.getReferenceById(id);
	}

	@Override
	public Category findById(Long id) {
		return repository.findById(id).orElseThrow(() -> BusinessException.of(ErrorCode.CATEGORY_NOT_FOUND));
	}

	@Override
	public Category findByName(String name) {
		return repository.findByName(name).orElseThrow(() -> BusinessException.of(ErrorCode.CATEGORY_NOT_FOUND));
	}

	@Override
	public Category findBySlug(String slug) {
		return repository.findBySlug(slug).orElseThrow(() -> BusinessException.of(ErrorCode.CATEGORY_NOT_FOUND));
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
