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
public class VariantResponse {
    Long id;
    String sku;
    BigDecimal price;
    Integer stockQuantity;
    String imageUrl;
    Set<AttributeWithTermResponse> attributes;
}
