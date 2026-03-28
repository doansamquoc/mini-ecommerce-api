package com.sam.miniecommerceapi.order.service;

import com.sam.miniecommerceapi.order.dto.request.CancelOrderRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderCreationRequest;
import com.sam.miniecommerceapi.order.entity.Order;

public interface OrderValidationService {

    void validateCancellation(Order order, CancelOrderRequest request);

    void validateDelivery(Order order);

    void validateOrderCreation(Long userId, OrderCreationRequest request);
}
