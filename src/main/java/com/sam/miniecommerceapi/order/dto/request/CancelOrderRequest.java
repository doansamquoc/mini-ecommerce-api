package com.sam.miniecommerceapi.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CancelOrderRequest {
    @NotBlank(message = "ORDER_CANCELLATION_REASEON_CANNOT_BE_NULL")
    String cancellationReason;
}
