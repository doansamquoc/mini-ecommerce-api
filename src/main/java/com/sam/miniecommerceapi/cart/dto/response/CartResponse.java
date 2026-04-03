package com.sam.miniecommerceapi.cart.dto.response;

import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    Long id;
    VariantResponse variant;
    Integer quantity;
}
