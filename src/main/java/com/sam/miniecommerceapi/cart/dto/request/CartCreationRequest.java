package com.sam.miniecommerceapi.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartCreationRequest {
    @NotNull(message = "product.validation.id_required")
    Long productVariantId;

    @NotNull(message = "cart.validation.quantity_required")
    @Min(value = 1, message = "cart.validation.quantity_size")
    Integer quantity;
}
