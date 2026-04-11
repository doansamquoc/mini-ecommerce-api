package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantUpdateRequest {
	@NotBlank(message = "product.slug.required")
	@Size(min = 2, max = 16, message = "product.slug.size")
	String sku;

	@NotNull(message = "product.price.required")
	@Min(value = 1000, message = "product.price.min")
	BigDecimal price;

	@NotNull(message = "product.stock.required")
	@PositiveOrZero(message = "product.stock.min")
	Integer stockQuantity;

	@NotNull(message = "product.iamge_id.required")
	Long imageId;

	Map<String, Object> attributes;
}
