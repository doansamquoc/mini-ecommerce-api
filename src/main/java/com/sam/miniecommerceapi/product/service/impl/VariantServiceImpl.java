package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.product.dto.request.VariantCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantRequest;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.Variant;
import com.sam.miniecommerceapi.product.mapper.VariantMapper;
import com.sam.miniecommerceapi.product.repository.VariantRepository;
import com.sam.miniecommerceapi.product.service.AttributeDefinitionService;
import com.sam.miniecommerceapi.product.service.ProductService;
import com.sam.miniecommerceapi.product.service.VariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VariantServiceImpl implements VariantService {
	VariantMapper mapper;
	VariantRepository repository;
	ProductService productService;
	AttributeDefinitionService definitionService;

	@Override
	public VariantResponse update(Long productId, Long id, VariantRequest request) {
		if (existsBySku(request.getSku())) {
			throw BusinessException.of(ErrorCode.SKU_ALREADY_EXISTS, "sku", "product.sku.conflict", request.getSku());
		}
		Variant variant = findByProductIdAndId(productId, id);
		mapper.toUpdate(request, variant);
		return mapper.toResponse(save(variant));
	}

	@Override
	public VariantResponse create(Long productId, VariantCreationRequest request) {
		if (existsBySku(request.sku())) {
			throw BusinessException.of(ErrorCode.SKU_ALREADY_EXISTS, "sku", "product.sku.conflict", request.sku());
		}

		Product product = productService.findById(productId);
		definitionService.attributeNormalizer(product.getCategory().getId(), request.attributes());

		Variant variant = mapper.toEntity(request);
		variant.setProduct(product);

		return mapper.toResponse(save(variant));
	}

	@Override
	public VariantResponse read(Long productId, Long variantId) {
		Variant variant = findByProductIdAndId(productId, variantId);
		return mapper.toResponse(variant);
	}

	@Override
	public List<VariantResponse> readAll(Long productId) {
		List<Variant> variants = findAllByProductId(productId);
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

	List<Variant> findAllByProductId(Long productId) {
		return repository.findAllByProductId(productId);
	}

	Variant findByProductIdAndId(Long productId, Long id) {
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
	public Variant findById(Long variantId) {
		return repository.findById(variantId).orElseThrow(
			() -> BusinessException.of(ErrorCode.PRODUCT_VARIANT_NOT_FOUND)
		);
	}

	@Override
	public Variant findGraphById(Long id) {
		return repository.findGraphById(id).orElseThrow(
			() -> BusinessException.of(ErrorCode.PRODUCT_VARIANT_NOT_FOUND)
		);
	}

	public List<Variant> saveAll(List<Variant> variants) {
		return repository.saveAll(variants);
	}

	public Variant save(Variant variant) {
		return repository.save(variant);
	}

	public boolean existsById(Long id) {
		return repository.existsById(id);
	}

	public List<Variant> findAllByIds(List<Long> productVariantIds) {
		return repository.findAllById(productVariantIds);
	}
}
