package com.sam.miniecommerceapi.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartCreationRequest {
    @NotNull(message = "CART_PRODUCT_ID_CANNOT_BE_NULL")
    Long productVariantId;

    @NotNull(message = "CART_QUANTITY_CANNOT_BE_NULL")
    Integer quantity;
}
