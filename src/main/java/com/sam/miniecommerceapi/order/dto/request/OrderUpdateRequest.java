package com.sam.miniecommerceapi.order.dto.request;

import com.sam.miniecommerceapi.common.constant.OrderStatus;
import com.sam.miniecommerceapi.common.constant.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {
	String phoneNumber;
	String shippingAddress;
	OrderStatus status;
	PaymentMethod paymentMethod;
}
