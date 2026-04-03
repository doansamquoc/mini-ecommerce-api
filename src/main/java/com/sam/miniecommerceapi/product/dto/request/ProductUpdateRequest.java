package com.sam.miniecommerceapi.product.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    String name;
    String description;
    String slug;
    Long categoryId;
    String mainImage;
    Set<VariantUpdateRequest> variants;
}
