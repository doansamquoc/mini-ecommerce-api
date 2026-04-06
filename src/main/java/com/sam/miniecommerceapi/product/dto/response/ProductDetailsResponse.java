package com.sam.miniecommerceapi.product.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

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
    BigDecimal regularPrice;
    String imageUrl;
    CategoryResponse category;
    List<VariantResponse> variants;
    List<AttributeResponse> attributes;
}
