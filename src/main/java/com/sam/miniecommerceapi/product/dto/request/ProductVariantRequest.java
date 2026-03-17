package com.sam.miniecommerceapi.product.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantRequest {
    String sku;
    BigDecimal price;
    Integer stockQuantity;
    String variantImageUrl;
    List<Long> attributeOptionIds;
}
