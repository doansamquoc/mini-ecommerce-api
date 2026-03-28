package com.sam.miniecommerceapi.order.service.impl;

import com.sam.miniecommerceapi.common.enums.OrderStatus;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
import com.sam.miniecommerceapi.order.entity.Order;
import com.sam.miniecommerceapi.order.helper.OrderHelper;
import com.sam.miniecommerceapi.order.mapper.OrderMapper;
import com.sam.miniecommerceapi.order.service.OrderDeliveryService;
import com.sam.miniecommerceapi.order.service.OrderValidationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDeliveryServiceImpl implements OrderDeliveryService {
    OrderMapper mapper;
    OrderHelper orderHelper;
    OrderValidationService validationService;

    @Override
    public OrderResponse deliveryOrder(Long orderId) {
        // Find order
        Order order = orderHelper.findById(orderId);

        // Validate
        validationService.validateDelivery(order);

        // Update
        order.setStatus(OrderStatus.DELIVERING);
        order.setDeliveredAt(Instant.now());

        return mapper.toResponse(orderHelper.save(order));
    }
}
