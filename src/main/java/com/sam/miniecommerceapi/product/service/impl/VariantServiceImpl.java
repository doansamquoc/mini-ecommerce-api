package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import com.sam.miniecommerceapi.shared.exception.BusinessException;
import com.sam.miniecommerceapi.product.dto.request.VariantUpdateRequest;
import com.sam.miniecommerceapi.product.entity.Variant;
import com.sam.miniecommerceapi.product.mapper.VariantMapper;
import com.sam.miniecommerceapi.product.repository.VariantRepository;
import com.sam.miniecommerceapi.product.service.VariantService;
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

    @Override
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }

    /**
     * Purpose: Avoid N + 1 query by check all of sku in once query.<br>
     * Check sku already exists. If existed throw an exception with that sku(s).
     *
     * @param skus List of sku (String)
     */
    @Override
    public void validateSkuNotExists(List<String> skus) {
        List<String> existedSkus = repository.findExistingSkus(skus);
        if (!existedSkus.isEmpty()) {
            throw new BusinessException(
                    ErrorCode.PRODUCT_SKU_ALREADY_EXISTS, Map.of("skus", existedSkus));
        }
    }

    /**
     * Get all variants by product ID
     *
     * @param id Product ID
     * @return List variants by product ID
     */
    @Override
    public List<Variant> getProductVariantsByProductId(Long id) {
        return repository.findAllByProductId(id);
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
    public void updateVariant(Variant variant, VariantUpdateRequest r) {
        Variant newVariant = mapper.toEntity(r, variant);
        repository.save(newVariant);
    }

    @Override
    public Variant findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
    }

    @Override
    public void deleteVariant(Variant variant) {
        repository.delete(variant);
    }

    @Override
    public List<Variant> saveAll(List<Variant> variants) {
        return repository.saveAll(variants);
    }

    @Override
    public Variant save(Variant variant) {
        return repository.save(variant);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<Variant> findAllByIds(List<Long> productVariantIds) {
        return repository.findAllById(productVariantIds);
    }
}
