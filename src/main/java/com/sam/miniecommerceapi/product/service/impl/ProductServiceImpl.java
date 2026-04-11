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
import com.sam.miniecommerceapi.upload.entity.Image;
import com.sam.miniecommerceapi.upload.service.ImageService;
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
	ImageService imageService;
	ProductRepository repository;
	CategoryService categoryService;

	@Override
	public ProductResponse createProduct(ProductCreationRequest req) {
		Category category = categoryService.findById(req.categoryId());
		Image image = imageService.findById(req.imageId());

		Product product = mapper.toEntity(req);
		product.setCategory(category);
		product.setSlug(makeUniqueSlug(product.getName()));
		product.setImage(image);

		return mapper.toResponse(save(product));
	}

	@Override
	public ProductResponse updateProduct(Long id, ProductUpdateRequest req) {
		Product product = findById(id);
		mapper.toUpdate(product, req);

		if (req.categoryId() != null) {
			Category category = categoryService.findById(req.categoryId());
			product.setCategory(category);
		}

		// Just update slug when update name too
		if (req.name() != null && !product.getName().equals(req.name())) {
			product.setSlug(makeUniqueSlug(req.name()));
		}

		if (req.imageId()!=null) {
			Image image = imageService.findById(req.imageId());
			product.setImage(image);
		}

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
