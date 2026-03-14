package com.sam.miniecommerceapi.product.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreationRequest {
    String name;
    String slug;
    String imageUrl;
}
