package com.sam.miniecommerceapi.order.service;

import com.sam.miniecommerceapi.order.dto.response.OrderResponse;

public interface OrderDeliveryService {
    OrderResponse deliveryOrder(Long orderId);
}
