package com.sam.miniecommerceapi.product.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSummaryResponse {
    Long id;
    String name;
    String slug;
    String images;
    String categoryName;
    BigDecimal displayPrice;
}
