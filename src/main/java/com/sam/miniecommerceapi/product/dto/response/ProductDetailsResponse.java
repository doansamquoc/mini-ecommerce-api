package com.sam.miniecommerceapi.product.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
    List<ProductVariantResponse> variants;
    List<ProductAttributeResponse> attributes;
}
