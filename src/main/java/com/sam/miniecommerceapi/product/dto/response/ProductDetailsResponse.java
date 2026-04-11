package com.sam.miniecommerceapi.product.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailsResponse {
	Long id;
	String name;
	String slug;
	String description;
	BigDecimal price;
	String imageUrl;
	CategoryResponse category;
	Set<VariantResponse> variants;
	Map<String, Set<Object>> attributes;
}
