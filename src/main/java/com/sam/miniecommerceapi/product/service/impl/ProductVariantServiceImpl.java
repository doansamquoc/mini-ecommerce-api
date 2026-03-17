package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.product.repository.ProductVariantRepository;
import com.sam.miniecommerceapi.product.service.ProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantServiceImpl implements ProductVariantService {
    ProductVariantRepository repository;

    @Override
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }

    /**
     * Purpose: Avoid N + 1 query by check all of sku in once query.<br/>
     * Check sku already exists. If existed throw an exception with that sku(s).
     *
     * @param skus List of sku (String)
     */
    @Override
    public void validateSkuNotExists(List<String> skus) {
        List<String> existedSkus = repository.findExistingSkus(skus);
        if (!existedSkus.isEmpty()) {
            throw new BusinessException(ErrorCode.PRODUCT_SKU_ALREADY_EXISTS, Map.of("skus", existedSkus));
        }
    }
}
