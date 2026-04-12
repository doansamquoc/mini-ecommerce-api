package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.common.dto.response.PageResponse;
import com.sam.miniecommerceapi.product.dto.SearchDTO;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductSearchService {
	PageResponse<SearchDTO> searchProducts(String keyword, BigDecimal minPrice, BigDecimal maxPrice, String categoryName, Pageable pageable);
}
