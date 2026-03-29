package com.sam.miniecommerceapi.order.dto.request;

import com.sam.miniecommerceapi.shared.constant.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    @NotBlank(message = "FIELD_REQUIRED")
    String shippingAddress;
    @NotBlank(message = "FIELD_REQUIRED")
    String phoneNumber;
    @NotNull(message = "FIELD_REQUIRED")
    PaymentMethod paymentMethod;
    @Valid
    @NotEmpty(message = "FIELD_REQUIRED")
    List<OrderItemRequest> items;
}
