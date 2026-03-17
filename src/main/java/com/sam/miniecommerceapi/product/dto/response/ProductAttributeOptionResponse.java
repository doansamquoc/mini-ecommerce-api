package com.sam.miniecommerceapi.product.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAttributeOptionResponse {
    String attribute;
    String value;
}
