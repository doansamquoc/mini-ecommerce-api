package com.sam.miniecommerceapi.order.service;

import com.sam.miniecommerceapi.order.dto.request.CancelOrderRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    @Transactional
    OrderResponse createOrder(Long userId, OrderRequest r);

    OrderResponse cancelOrder(Long orderId, CancelOrderRequest request);

    OrderResponse confirmOrder(Long orderId);

    OrderResponse deliveringOrder(Long orderId);

    OrderResponse deliveredOrder(Long orderId);

    OrderResponse paymentPendingOrder(Long orderId);

    OrderResponse paymentPaidOrder(Long orderId);

    OrderResponse paymentFailedOrder(Long orderId);

    OrderResponse updateOrder(Long id, OrderRequest r);
}
