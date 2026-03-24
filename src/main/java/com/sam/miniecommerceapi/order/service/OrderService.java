package com.sam.miniecommerceapi.order.service;

import com.sam.miniecommerceapi.order.dto.request.OrderRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    @Transactional
    OrderResponse createOrder(Long userId, OrderRequest r);

    OrderResponse updateOrder(Long id, OrderRequest r);
}
