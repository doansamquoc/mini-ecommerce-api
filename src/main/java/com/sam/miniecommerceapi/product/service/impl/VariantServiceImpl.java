package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.product.dto.request.VariantRequest;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.Variant;
import com.sam.miniecommerceapi.product.mapper.VariantMapper;
import com.sam.miniecommerceapi.product.repository.VariantRepository;
import com.sam.miniecommerceapi.product.service.ProductService;
import com.sam.miniecommerceapi.product.service.VariantService;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import com.sam.miniecommerceapi.shared.exception.BusinessException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VariantServiceImpl implements VariantService {
    VariantMapper mapper;
    VariantRepository repository;
    ProductService productService;

    @Override
    public VariantResponse update(Long productId, Long id, VariantRequest request) {
        if (existsBySku(request.getSku())) throw new BusinessException(ErrorCode.PRODUCT_SLUG_CONFLICT);
        Variant variant = findByProductIdAndId(productId, id);
        mapper.toUpdate(request, variant);
        return mapper.toResponse(save(variant));
    }

    @Override
    public VariantResponse create(Long productId, VariantRequest request) {
        if (existsBySku(request.getSku())) throw new BusinessException(ErrorCode.PRODUCT_SLUG_CONFLICT);
        Product product = productService.findById(productId);
        Variant variant = mapper.toUpdate(request);
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
                () -> new BusinessException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND)
        );
    }

    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }

    public void validateSkuNotExists(List<String> skus) {
        List<String> existedSkus = repository.findExistingSkus(skus);
        if (!existedSkus.isEmpty()) {
            throw new BusinessException(
                    ErrorCode.PRODUCT_SKU_ALREADY_EXISTS, Map.of("skus", existedSkus));
        }
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
        return repository.findById(variantId).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
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
