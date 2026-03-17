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
public class ProductCreationRequest {
    String name;
    String description;
    String slug;
    BigDecimal minPrice;
    String categoryId;
    String mainImage;
    List<ProductVariantRequest> variants;
}
