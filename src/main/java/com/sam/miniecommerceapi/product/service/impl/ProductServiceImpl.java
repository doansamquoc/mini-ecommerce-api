package com.sam.miniecommerceapi.product.service.impl;

import com.github.slugify.Slugify;
import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.util.UUIDUtils;
import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.Category;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.mapper.ProductMapper;
import com.sam.miniecommerceapi.product.repository.ProductRepository;
import com.sam.miniecommerceapi.product.service.CategoryService;
import com.sam.miniecommerceapi.product.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
	Slugify slugify;
	ProductMapper mapper;
	ProductRepository repository;
	CategoryService categoryService;

	@Override
	public ProductResponse createProduct(ProductCreationRequest request) {
		Category category = categoryService.findById(request.categoryId());
		Product product = mapper.toEntity(request);
		product.setCategory(category);
		product.setSlug(makeUniqueSlug(product.getName()));
		return mapper.toResponse(save(product));
	}

	@Override
	public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
		Product product = findById(id);

		if (request.categoryId() != null) {
			Category category = categoryService.findById(request.categoryId());
			product.setCategory(category);
		}

		if (request.name() != null && !product.getName().equals(request.name())) {
			product.setSlug(makeUniqueSlug(request.name()));
		}

		mapper.toUpdate(product, request);
		return mapper.toResponse(save(product));
	}

	@Transactional(readOnly = true)
	@Override
	public ProductDetailsResponse getProductDetailsById(Long id) {
		Product product = findDetailsById(id);
		return mapper.toDetailsResponse(product);
	}

	@Transactional(readOnly = true)
	@Override
	public ProductDetailsResponse getProductDetailsBySlug(String slug) {
		Product product = findBySlug(slug);
		return mapper.toDetailsResponse(product);
	}

	@Transactional
	@Override
	public void deleteProduct(Long id) {
		delete(id);
	}

	@Transactional
	@Override
	public void deleteAllProduct() {
		deleteAll();
	}

	private void delete(Long id) {
		repository.deleteById(id);
	}

	private void deleteAll() {
		repository.deleteAll();
	}

	private Product save(Product product) {
		return repository.save(product);
	}

	@Override
	public Product findById(Long id) {
		return repository.findById(id).orElseThrow(() -> BusinessException.of(ErrorCode.PRODUCT_NOT_FOUND));
	}

	public Product findDetailsById(Long id) {
		return repository.findDetailsById(id).orElseThrow(() -> BusinessException.of(ErrorCode.PRODUCT_NOT_FOUND));
	}

	private Product findBySlug(String slug) {
		return repository.findBySlug(slug).orElseThrow(() -> BusinessException.of(ErrorCode.PRODUCT_NOT_FOUND));
	}

	private String makeUniqueSlug(String input) {
		return slugify.slugify(input) + "-" + UUIDUtils.generateSlugSuffix();
	}
}
