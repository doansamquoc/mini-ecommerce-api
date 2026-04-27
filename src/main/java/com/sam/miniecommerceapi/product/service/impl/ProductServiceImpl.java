package com.sam.miniecommerceapi.product.service.impl;

import com.github.slugify.Slugify;
import com.sam.miniecommerceapi.common.constant.CacheNames;
import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.util.UUIDUtils;
import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.Category;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.ProductOption;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.product.mapper.ProductMapper;
import com.sam.miniecommerceapi.product.repository.ProductRepository;
import com.sam.miniecommerceapi.product.service.CategoryService;
import com.sam.miniecommerceapi.product.service.ProductOptionService;
import com.sam.miniecommerceapi.product.service.ProductService;
import com.sam.miniecommerceapi.product.service.ProductVariantService;
import com.sam.miniecommerceapi.upload.entity.Image;
import com.sam.miniecommerceapi.upload.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;

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
	ProductOptionService optionService;
	ProductVariantService variantService;

	@Override
	public ProductResponse insert(ProductCreationRequest req) {
		Product product = mapper.toEntity(req);

		// Set category
		product.setCategory(categoryService.getReference(req.categoryId()));

		// Set image
		product.setImage(imageService.getReference(req.imageId()));

		// Set Slug
		product.setSlug(makeUniqueSlug(req.name()));

		// Set all variants. Simply save the product and its variations will be saved as well.
		List<ProductVariant> variants = variantService.mapVariants(req.variants());
		variants.forEach(variant -> variant.setProduct(product)); // Many to many
		product.setVariants(new LinkedHashSet<>(variants)); // Many to many

		// Set all options. Simply save the product and its options will be saved as well.
		List<ProductOption> options = optionService.mapOptions(req.options());
		options.forEach(option -> option.setProduct(product)); // Many to many
		product.setOptions(options); // Many to many

		// Save and return
		return mapper.toResponse(repository.save(product));
	}

	@Override
	@CacheEvict(value = CacheNames.PRODUCT)
	public ProductResponse createProduct(ProductCreationRequest req) {
		Category category = categoryService.getReference(req.categoryId());
		Image image = imageService.getReference(req.imageId());

		Product product = mapper.toEntity(req);
		product.setCategory(category);
		product.setSlug(makeUniqueSlug(product.getName()));
		product.setImage(image);

		return mapper.toResponse(save(product));
	}

	@Override
	@Caching(evict = {
		@CacheEvict(value = CacheNames.PRODUCT, key = "#id"),
		@CacheEvict(value = CacheNames.PRODUCT, key = "#result.slug"),
		@CacheEvict(value = CacheNames.PRODUCTS_LIST, allEntries = true)
	})
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

		if (req.imageId() != null) {
			Image image = imageService.findById(req.imageId());
			product.setImage(image);
		}

		return mapper.toResponse(save(product));
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = CacheNames.PRODUCT, key = "#slug")
	public ProductDetailsResponse getProductDetailsBySlug(String slug) {
		Product product = findBySlug(slug);
		return mapper.toDetailsResponse(product);
	}

	@Override
	@Transactional
	@Caching(evict = {
		@CacheEvict(value = CacheNames.PRODUCT, key = "#id"),
		@CacheEvict(value = CacheNames.PRODUCT, allEntries = true)
	})
	public void deleteProduct(Long id) {
		delete(id);
	}

	@Override
	@Transactional
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

	private Product findBySlug(String slug) {
		return repository.findBySlug(slug).orElseThrow(() -> BusinessException.of(ErrorCode.PRODUCT_NOT_FOUND));
	}

	private String makeUniqueSlug(String input) {
		return slugify.slugify(input) + "-" + UUIDUtils.generateSlugSuffix();
	}
}
