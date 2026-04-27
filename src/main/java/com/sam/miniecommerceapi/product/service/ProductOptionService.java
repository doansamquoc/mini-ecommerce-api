package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.ProductOptionDto;
import com.sam.miniecommerceapi.product.entity.ProductOption;

import java.util.List;

public interface ProductOptionService {
	List<ProductOption> mapOptions(List<ProductOptionDto> reqs);
}
