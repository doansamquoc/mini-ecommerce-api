package com.sam.miniecommerceapi.order.dto.response;

import com.sam.miniecommerceapi.common.enums.OrderStatus;
import com.sam.miniecommerceapi.common.enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    BigDecimal totalPrice;
    LocalDateTime orderDate;
    OrderStatus status;
    String shippingAddress;
    String phoneNumber;
    PaymentMethod paymentMethod;
    List<OrderItemResponse> items;
}
