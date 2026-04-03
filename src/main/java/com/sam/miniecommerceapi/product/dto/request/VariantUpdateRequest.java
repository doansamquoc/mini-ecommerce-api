package com.sam.miniecommerceapi.product.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantUpdateRequest {
    Long id; // IMPORTANT: null = create new, not null = update
    String sku;
    BigDecimal price;
    Integer stockQuantity;
    String variantImageUrl;
    Set<Long> attributeOptionIds;
}
