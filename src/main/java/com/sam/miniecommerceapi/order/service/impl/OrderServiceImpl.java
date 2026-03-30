package com.sam.miniecommerceapi.order.service.impl;

import com.sam.miniecommerceapi.order.dto.request.CancelOrderRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderItemRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
import com.sam.miniecommerceapi.order.entity.Order;
import com.sam.miniecommerceapi.order.entity.OrderItem;
import com.sam.miniecommerceapi.order.mapper.OrderMapper;
import com.sam.miniecommerceapi.order.repository.OrderRepository;
import com.sam.miniecommerceapi.order.service.OrderItemService;
import com.sam.miniecommerceapi.order.service.OrderService;
import com.sam.miniecommerceapi.order.util.OrderStateMachine;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.product.service.ProductVariantService;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import com.sam.miniecommerceapi.shared.constant.OrderStatus;
import com.sam.miniecommerceapi.shared.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.shared.exception.BusinessException;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderMapper mapper;
    UserService userService;
    OrderRepository repository;
    OrderItemService itemService;
    ProductVariantService variantService;

    @Override
    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest r) {
        User user = userService.findById(userId);
        Order order = mapper.toEntity(r);
        order.setUser(user);
        order.setOrderItems(new ArrayList<>());

        for (OrderItemRequest i : r.getItems()) {
            // Receiver the record number has been updated
            int affected = variantService.deductStock(i.getVariantId(), i.getQuantity());
            // If the record not change
            if (affected == 0) {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK, Map.of("id", i.getVariantId()));
            }

            ProductVariant variant = variantService.findById(i.getVariantId());
            OrderItem orderItem = createOrderItem(order, variant, i.getQuantity());

            order.addToOrderItems(orderItem);
        }

        order.calcTotalPrice();
        return mapper.toResponse(repository.save(order));
    }

    @Override
    public OrderResponse cancelOrder(Long orderId, CancelOrderRequest request) {
        Order order = findById(orderId);
        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.CANCELED, order.getPaymentMethod())) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_BE_CANCELED);
        }
        order.cancel(request.getCancellationReason());
        return mapper.toResponse(repository.save(order));
    }

    @Override
    public OrderResponse confirmOrder(Long orderId) {
        Order order = findById(orderId);
        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.CONFIRMED, order.getPaymentMethod())) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_BE_CONFIRMED);
        }
        order.markAsConfirm();
        return mapper.toResponse(repository.save(order));
    }

    @Override
    public OrderResponse deliveringOrder(Long orderId) {
        Order order = findById(orderId);
        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.DELIVERING, order.getPaymentMethod())) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_BE_DELIVERING);
        }
        order.markAsDelivering();
        return mapper.toResponse(repository.save(order));
    }

    @Override
    public OrderResponse deliveredOrder(Long orderId) {
        Order order = findById(orderId);
        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.DELIVERED, order.getPaymentMethod())) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_BE_DELIVERED);
        }
        order.delivered();
        return mapper.toResponse(repository.save(order));
    }

    @Override
    public OrderResponse paymentPendingOrder(Long orderId) {
        Order order = findById(orderId);
        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.PENDING_PAYMENT, order.getPaymentMethod())) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_BE_PAYMENT_PENDING);
        }
        order.markAsPendingPayment();
        return mapper.toResponse(repository.save(order));
    }

    @Override
    public OrderResponse paymentPaidOrder(Long orderId) {
        Order order = findById(orderId);
        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.PENDING_PAYMENT, order.getPaymentMethod())) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_BE_PAID);
        }
        order.markAsPaid();
        return mapper.toResponse(repository.save(order));
    }

    @Override
    public OrderResponse paymentFailedOrder(Long orderId) {
        Order order = findById(orderId);
        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.PENDING_PAYMENT, order.getPaymentMethod())) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_BE_FAILED);
        }
        order.markAsFailed();
        return mapper.toResponse(repository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        Order order = findById(id);
        mapper.toEntity(request, order);

        Map<Long, OrderItem> existingItemsMap = order.getOrderItems().stream()
                .collect(Collectors.toMap(item -> item.getVariant().getId(), item -> item));

        for (OrderItemRequest itemRequest : request.getItems()) {
            // Order must be at least 1 product
            if (itemRequest.getQuantity() < 1) {
                throw new BusinessException(
                        ErrorCode.ORDER_INVALID_QUANTITY, Map.of("variantId", itemRequest.getVariantId())
                );
            }

            // Check current item already exists yet
            OrderItem existingItem = existingItemsMap.get(itemRequest.getVariantId());
            if (existingItem != null) {
                int quantityDiff = itemRequest.getQuantity() - existingItem.getQuantity();
                if (quantityDiff > 0) {
                    int affected = variantService.deductStock(itemRequest.getVariantId(), itemRequest.getQuantity());
                    if (affected == 0) {
                        throw new BusinessException(
                                ErrorCode.PRODUCT_OUT_OF_STOCK, Map.of("variantId", itemRequest.getVariantId())
                        );
                    }
                } else if (quantityDiff < 0) {
                    variantService.increaseStock(itemRequest.getVariantId(), Math.abs(quantityDiff));
                }

                existingItem.setQuantity(itemRequest.getQuantity());
                existingItemsMap.remove(itemRequest.getVariantId());
            } else {
                int affected = variantService.deductStock(itemRequest.getVariantId(), itemRequest.getQuantity());
                if (affected == 0) {
                    throw new BusinessException(
                            ErrorCode.PRODUCT_OUT_OF_STOCK,
                            Map.of("variantId", itemRequest.getVariantId())
                    );
                }

                ProductVariant variant = variantService.findById(itemRequest.getVariantId());
                OrderItem newItem = createOrderItem(order, variant, itemRequest.getQuantity());

                order.addToOrderItems(newItem);
            }
        }

        List<OrderItem> removedItems = new ArrayList<>();
        for (OrderItem removedItem : existingItemsMap.values()) {
            variantService.increaseStock(removedItem.getVariant().getId(), removedItem.getQuantity());
            order.removeOrderItem(removedItem);
            removedItems.add(removedItem);
        }
        itemService.deleteAll(removedItems);

        order.calcTotalPrice();
        return mapper.toResponse(repository.save(order));
    }

    @Override
    public PageResponse<OrderResponse> getOrderByStatus(OrderStatus status, Pageable pageable) {
        Page<Order> orders = repository.findByStatus(status, pageable);
        Page<OrderResponse> responses = orders.map(mapper::toResponse);
        return PageResponse.from(responses);
    }

    @Override
    public PageResponse<OrderResponse> getOrderByUserId(Long userId, OrderStatus status, Pageable pageable) {
        Page<Order> orders = repository.findByUserIdAndStatus(userId, status, pageable);
        Page<OrderResponse> responses = orders.map(mapper::toResponse);
        return PageResponse.from(responses);
    }


    public Order findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
    }

    private OrderItem createOrderItem(Order order, ProductVariant variant, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setVariant(variant);
        orderItem.setOrder(order);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(variant.getPrice());
        orderItem.calcTotalPrice();

        return orderItem;
    }
}
