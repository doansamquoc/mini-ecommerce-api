package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.ProductOptionDto;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.ProductOption;

import java.util.List;

public interface ProductOptionService {
	void batchUpdate(Product product, List<ProductOptionDto> dtos);

	List<ProductOption> mappingOptions(List<ProductOptionDto> reqs);
}
