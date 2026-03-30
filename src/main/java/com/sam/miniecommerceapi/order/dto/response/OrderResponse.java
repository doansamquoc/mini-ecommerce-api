package com.sam.miniecommerceapi.order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sam.miniecommerceapi.shared.constant.OrderStatus;
import com.sam.miniecommerceapi.shared.constant.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    Long id;
    BigDecimal totalPrice;
    Instant orderDate;
    OrderStatus status;
    String shippingAddress;
    String phoneNumber;
    PaymentMethod paymentMethod;
    List<OrderItemResponse> orderItems;
    String canceledReason;
    Instant canceledAt;
    Instant deliveredAt;
}
