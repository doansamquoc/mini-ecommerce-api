package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantUpdateRequest {
	Long id;
	String sku;
	@Min(value = 1000, message = "product.validation.price_min")
	BigDecimal price;
	@PositiveOrZero(message = "product.validation.stock_min")
	Integer stockQuantity;
	@URL(message = "product.validation.image_invalid_url")
	String imageUrl;
	Set<Long> attributeValueIds;
}
