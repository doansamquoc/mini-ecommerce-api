package com.sam.miniecommerceapi.product.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Long id;
    String name;
    String slug;
    String description;
    List<ProductVariantResponse> variants;
    List<ProductAttributeResponse> attributes;
}
