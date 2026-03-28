package com.sam.miniecommerceapi.order.service.impl;

import com.sam.miniecommerceapi.common.enums.OrderStatus;
import com.sam.miniecommerceapi.order.dto.request.CancelOrderRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
import com.sam.miniecommerceapi.order.entity.Order;
import com.sam.miniecommerceapi.order.entity.OrderItem;
import com.sam.miniecommerceapi.order.helper.OrderHelper;
import com.sam.miniecommerceapi.order.mapper.OrderMapper;
import com.sam.miniecommerceapi.order.repository.OrderRepository;
import com.sam.miniecommerceapi.order.service.OrderCancellationService;
import com.sam.miniecommerceapi.order.service.OrderValidationService;
import com.sam.miniecommerceapi.product.service.ProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderCancellationServiceImpl implements OrderCancellationService {
    OrderMapper mapper;
    OrderHelper orderHelper;
    OrderRepository repository;
    ProductVariantService variantService;
    OrderValidationService validationService;

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId, CancelOrderRequest request) {
        // Get order
        Order order = orderHelper.findById(orderId);

        // Validate
        validationService.validateCancellation(order, request);

        // Return stock
        returnStock(order);

        // Process refund

        // Update order
        order.setStatus(OrderStatus.CANCELLED);
        order.setCanceledAt(Instant.now());
        order.setCanceledReason(request.getCancellationReason());

        // Notification

        return mapper.toResponse(repository.save(order));
    }

    private void returnStock(Order order) {
        Map<Long, Integer> items = order.getOrderItems()
                .stream()
                .collect(Collectors.toMap(
                        item -> item.getVariant().getId(),
                        OrderItem::getQuantity,
                        Integer::sum)
                );
        items.forEach(variantService::increaseStock);
    }
}
