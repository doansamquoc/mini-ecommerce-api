package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.exception.FieldViolation;
import com.sam.miniecommerceapi.product.dto.request.ProductVariantRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantCreationRequest;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.product.mapper.ProductVariantMapper;
import com.sam.miniecommerceapi.product.repository.ProductVariantRepository;
import com.sam.miniecommerceapi.product.service.ProductVariantService;
import com.sam.miniecommerceapi.upload.entity.Image;
import com.sam.miniecommerceapi.upload.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantServiceImpl implements ProductVariantService {
	ProductVariantMapper mapper;
	ImageService imageService;
	ProductVariantRepository repository;

	@Override
	public List<ProductVariant> mapVariants(List<VariantCreationRequest> reqs) {
		List<FieldViolation> violations = new ArrayList<>();
		violations.addAll(detectDuplicateSkus(reqs));
		violations.addAll(detectSkuConflicts(reqs));
		violations.addAll(validateImageIds(reqs));
		if (!violations.isEmpty()) throw BusinessException.of(ErrorCode.VALIDATION_FAILED, violations);

		List<ProductVariant> variants = reqs.stream().map(mapper::toEntity).toList();
		variants.forEach(v -> v.setTitle(makeTitle(v)));

		return variants;
	}

	private String makeTitle(ProductVariant variant) {
		return Stream.of(
			variant.getOption1(),
			variant.getOption2(),
			variant.getOption3()
		).filter(Objects::nonNull
		).collect(Collectors.joining(" / "));
	}

	private List<FieldViolation> detectSkuConflicts(List<VariantCreationRequest> reqs) {
		List<String> skus = reqs.stream().map(VariantCreationRequest::sku).filter(Objects::nonNull).toList();
		if (skus.isEmpty()) return Collections.emptyList();

		Set<String> existingSkus = new HashSet<>(findExistingSkus(skus));
		if (existingSkus.isEmpty()) return Collections.emptyList();

		return IntStream.range(0, reqs.size()).filter(i -> existingSkus.contains(reqs.get(i).sku())
		).mapToObj(i -> new FieldViolation(String.format("variants[%d].sku", i), "product.sku.conflict", reqs.get(i).sku())
		).toList();
	}

	private List<FieldViolation> validateImageIds(List<VariantCreationRequest> reqs) {
		List<Long> ids = reqs.stream().map(VariantCreationRequest::imageId).toList();
		if (ids.isEmpty()) return Collections.emptyList();

		Set<Long> existingIds = new HashSet<>(imageService.findExistingImages(ids));
		if (existingIds.isEmpty()) return Collections.emptyList();

		return IntStream.range(0, reqs.size()).filter(i -> {
			Long id = reqs.get(i).imageId();
			return id != null && !existingIds.contains(id);
		}).mapToObj(i -> new FieldViolation(
			String.format("variants[%d].imageId", i),
			"image.not_found",
			reqs.get(i).imageId()
		)).toList();
	}

	private List<FieldViolation> detectDuplicateSkus(List<VariantCreationRequest> reqs) {
		Map<String, List<Integer>> skuIndexes = new HashMap<>();
		for (int i = 0; i < reqs.size(); i++) {
			String sku = reqs.get(i).sku();
			if (sku != null) skuIndexes.computeIfAbsent(sku, k -> new ArrayList<>()).add(i);
		}

		return skuIndexes.values().stream().filter(indexes -> indexes.size() > 1
		).flatMap(indexes -> indexes.stream()
			.map(i -> new FieldViolation(String.format("variants[%d].sku", i), "product.sku.duplicate", reqs.get(i).sku()))
		).toList();
	}

	private List<String> findExistingSkus(List<String> skus) {
		List<String> existingSkus = repository.findExistingSkus(skus);

		// The existing skus
		return skus.stream().filter(existingSkus::contains).collect(Collectors.toList());
	}


	@Override
	public VariantResponse update(Long productId, Long id, ProductVariantRequest req) {
		if (!req.sku().isBlank() && existsBySku(req.sku())) {
			throw BusinessException.of(
				ErrorCode.SKU_ALREADY_EXISTS,
				new FieldViolation("sku", "product.sku.conflict", req.sku())
			);
		}

		ProductVariant variant = findByProductIdAndId(productId, id);
		mapper.toUpdate(req, variant);

		if (req.imageId() != null) {
			Image image = imageService.findById(req.imageId());
			variant.setImage(image);
		}

		return mapper.toResponse(save(variant));
	}

	@Override
	public ProductVariant mapUpdate(ProductVariantRequest req, ProductVariant variant) {
		return mapper.toUpdate(req, variant);
	}

	@Override
	public ProductVariant mapEntity(ProductVariantRequest req) {
		return mapper.toEntity(req);
	}

	@Override
	public void batchUpdate(Product product, List<ProductVariantRequest> reqs) {
		if (reqs == null || reqs.isEmpty()) {
			product.getVariants().clear();
			return;
		}

		Set<Long> requestIds = reqs.stream().map(ProductVariantRequest::id).collect(Collectors.toSet());
		product.getVariants().removeIf(variant -> !requestIds.contains(variant.getId()));

		Map<Long, ProductVariant> existing = product.getVariants().stream(
		).filter(v -> v.getId() != null
		).collect(Collectors.toMap(ProductVariant::getId, v -> v));

		for (ProductVariantRequest req : reqs) {
			if (req.id() != null && existing.containsKey(req.id())) {
				ProductVariant variant = existing.get(req.id());
				if (variant != null) mapper.toUpdate(req, variant);
			} else {
				ProductVariant variant = mapper.toEntity(req);
				variant.setProduct(product);
				product.getVariants().add(variant);
			}
		}
	}

	@Override
	public VariantResponse createVariant(Long productId, VariantCreationRequest req) {
		if (existsBySku(req.sku())) {
			throw BusinessException.of(
				ErrorCode.SKU_ALREADY_EXISTS,
				new FieldViolation("sku", "product.sku.conflict", req.sku())
			);
		}

		ProductVariant variant = mapper.toEntity(req);

		Image image = imageService.findById(req.imageId());
		variant.setImage(image);

		return mapper.toResponse(save(variant));
	}

	@Override
	public VariantResponse getVariant(Long productId, Long variantId) {
		ProductVariant variant = findByProductIdAndId(productId, variantId);
		return mapper.toResponse(variant);
	}

	@Override
	public List<VariantResponse> getAllVariants(Long productId) {
		List<ProductVariant> variants = findAllByProductId(productId);
		return mapper.toResponseList(variants);
	}

	@Override
	public void delete(Long productId, Long variantId) {
		repository.deleteByProductIdAndId(productId, variantId);
	}

	@Override
	public void deleteAll(Long productId) {
		repository.findAllByProductId(productId);
	}

	List<ProductVariant> findAllByProductId(Long productId) {
		return repository.findAllByProductId(productId);
	}

	ProductVariant findByProductIdAndId(Long productId, Long id) {
		return repository.findByProductIdAndId(productId, id).orElseThrow(
			() -> BusinessException.of(ErrorCode.PRODUCT_VARIANT_NOT_FOUND)
		);
	}

	public boolean existsBySku(String sku) {
		return repository.existsBySku(sku);
	}

	public void validateSkuNotExists(List<String> skus) {
		List<String> existedSkus = repository.findExistingSkus(skus);
		if (!existedSkus.isEmpty()) throw BusinessException.of(ErrorCode.SKU_ALREADY_EXISTS);
	}

	@Override
	public int deductStock(Long id, int quantity) {
		return repository.deductStock(id, quantity);
	}

	@Override
	public void increaseStock(Long id, int quantity) {
		repository.increaseStock(id, quantity);
	}

	@Override
	public ProductVariant findById(Long variantId) {
		return repository.findById(variantId).orElseThrow(
			() -> BusinessException.of(ErrorCode.PRODUCT_VARIANT_NOT_FOUND)
		);
	}

	@Override
	public ProductVariant findGraphById(Long id) {
		return repository.findGraphById(id).orElseThrow(
			() -> BusinessException.of(ErrorCode.PRODUCT_VARIANT_NOT_FOUND)
		);
	}

	public List<ProductVariant> saveAll(List<ProductVariant> variants) {
		return repository.saveAll(variants);
	}

	public ProductVariant save(ProductVariant variant) {
		return repository.save(variant);
	}

	public boolean existsById(Long id) {
		return repository.existsById(id);
	}

	public List<ProductVariant> findAllByIds(List<Long> productVariantIds) {
		return repository.findAllById(productVariantIds);
	}
}
