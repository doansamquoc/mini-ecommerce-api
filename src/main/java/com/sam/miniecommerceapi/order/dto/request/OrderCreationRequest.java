package com.sam.miniecommerceapi.order.dto.request;

import com.sam.miniecommerceapi.common.enums.OrderStatus;
import com.sam.miniecommerceapi.common.enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationRequest {
    String shippingAddress;
    String phoneNumber;
    OrderStatus status;
    PaymentMethod paymentMethod;
    List<OrderItemRequest> items;
}
