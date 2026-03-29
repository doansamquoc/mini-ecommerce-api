package com.sam.miniecommerceapi.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemRequest {
    @NotNull(message = "FIELD_REQUIRED")
    Long variantId;
    @Min(value = 1, message = "ORDER_ITEM_QUANTITY_MIN")
    Integer quantity;
}
