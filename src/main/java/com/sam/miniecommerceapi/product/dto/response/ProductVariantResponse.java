package com.sam.miniecommerceapi.product.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantResponse {
    String id;
    String sku;
    BigDecimal price;
    Integer stockQuantity;
    String variantImageUrl;
    Set<ProductAttributeOptionResponse> options;
}
