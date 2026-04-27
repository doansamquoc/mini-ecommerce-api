package com.sam.miniecommerceapi.product.dto;

import java.util.List;

public record ProductOptionDto(
	String name,
	List<String> values,
	Integer position
) {}
