package com.sam.miniecommerceapi.cart.dto.response;

import com.sam.miniecommerceapi.product.dto.response.ProductVariantResponse;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    Long id;
    ProductVariantResponse variant;
    Integer quantity;
}
