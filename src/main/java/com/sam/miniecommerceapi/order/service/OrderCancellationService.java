package com.sam.miniecommerceapi.order.service;

import com.sam.miniecommerceapi.order.dto.request.CancelOrderRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;

public interface OrderCancellationService {
    OrderResponse cancelOrder(Long orderId, CancelOrderRequest request);
}
