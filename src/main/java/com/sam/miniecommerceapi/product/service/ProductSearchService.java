package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.common.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductSearchService {
    PageResponse<ProductResponse> searchProducts(
            String keyword,
            BigDecimal minPrice,
            String categoryName,
            Pageable pageable
    );
}
