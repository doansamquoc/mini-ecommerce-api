package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.common.constant.CacheNames;
import com.sam.miniecommerceapi.common.constant.ErrorCode;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
	CategoryMapper mapper;
	CategoryRepository repository;
	private final ImageService imageService;

	@Override
	@Cacheable(value = CacheNames.CATEGORIES, key = "'all'", unless = "#result == null || #result.isEmpty()")
	public List<CategoryResponse> getAllCategories() {
		List<Category> categories = repository.findAll();
		return categories.stream().map(mapper::toResponse).toList();
	}

	@Override
	@Cacheable(value = CacheNames.CATEGORIES, key = "'id:' + #id", unless = "#result == null")
	public CategoryResponse getCategoryById(Long id) {
		return mapper.toResponse(findById(id));
	}

	@Override
	@Cacheable(value = CacheNames.CATEGORIES, key = "'slug:' + #slug", unless = "#result == null")
	public CategoryResponse getCategorySlug(String slug) {
		return mapper.toResponse(findBySlug(slug));
	}

	@Override
	@Cacheable(value = CacheNames.CATEGORIES, key = "'name:' + #name", unless = "#result == null")
	public CategoryResponse getCategoryByName(String name) {
		return mapper.toResponse(findByName(name));
	}

	@Override
	@CacheEvict(value = CacheNames.CATEGORIES, allEntries = true)
	public CategoryResponse createCategory(CategoryCreationRequest req) {
		if (existsByName(req.getName())) throw BusinessException.of(ErrorCode.CATEGORY_NAME_CONFLICT);
		if (existsBySlug(req.getSlug())) throw BusinessException.of(ErrorCode.CATEGORY_SLUG_CONFLICT);

		Image image = imageService.findById(req.getImageId());
		Category category = mapper.toEntity(req);
		category.setImage(image);

		return mapper.toResponse(repository.save(category));
	}

	@Override
	@Caching(evict = {
		@CacheEvict(value = CacheNames.CATEGORIES, allEntries = true),
		@CacheEvict(value = CacheNames.PRODUCT, allEntries = true)
	})
	public CategoryResponse updateCategory(Long id, CategoryUpdateRequest req) {
		Category category = findById(id);

		if (!req.getName().equals(category.getName())) {
			if (existsByNameAndIdNot(req.getName(), id))
				throw BusinessException.of(ErrorCode.CATEGORY_NAME_CONFLICT);
		}

		if (!req.getSlug().equals(category.getSlug())) {
			if (existsBySlugAndIdNot(req.getSlug(), id)) throw BusinessException.of(ErrorCode.CATEGORY_SLUG_CONFLICT);
		}

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
	public boolean existsByName(String name) {
		return repository.existsByName(name);
	}

	@Override
	public boolean existsBySlug(String slug) {
		return repository.existsBySlug(slug);
	}

	boolean existsByNameAndIdNot(String name, Long id) {
		return repository.existsByNameAndIdNot(name, id);
	}

	boolean existsBySlugAndIdNot(String slug, Long id) {
		return repository.existsBySlugAndIdNot(slug, id);
	}
}
