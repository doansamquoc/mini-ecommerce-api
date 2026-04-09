package com.sam.miniecommerceapi.product.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantResponse {
	Long id;
	String sku;
	BigDecimal price;
	Integer stockQuantity;
	String imageUrl;
	Map<String, Object> attributes;
}
