package com.sam.miniecommerceapi.order.service.impl;

import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.enums.OrderStatus;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.order.dto.request.OrderItemRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
import com.sam.miniecommerceapi.order.entity.Order;
import com.sam.miniecommerceapi.order.entity.OrderItem;
import com.sam.miniecommerceapi.order.mapper.OrderMapper;
import com.sam.miniecommerceapi.order.repository.OrderRepository;
import com.sam.miniecommerceapi.order.service.OrderItemService;
import com.sam.miniecommerceapi.order.service.OrderService;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.product.service.ProductVariantService;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Transactional
    @Override
    public OrderResponse createOrder(Long userId, OrderRequest r) {
        User user = userService.findById(userId);
        Order order = mapper.toEntity(r);
        order.setUser(user);
        order.setOrderItems(new ArrayList<>());

        for (OrderItemRequest i : r.getItems()) {
            // Receiver the record number has been updated
            int updateRows = variantService.deductStock(i.getVariantId(), i.getQuantity());

            // If the record not change
            if (updateRows == 0) {
                throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK, Map.of("id", i.getVariantId()));
            }

            ProductVariant variant = variantService.findById(i.getVariantId());

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .variant(variant)
                    .quantity(i.getQuantity())
                    .price(variant.getPrice())
                    .build();

            order.getOrderItems().add(orderItem);
        }


        order.setTotalPrice(calcTotalPrice(order));

        return mapper.toResponse(repository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest r) {
        Order order = findById(id);

        /*
         * Check condition on old order. after that use mapper map request into entity
         */

        // Orders can only be updated while the order status is pending
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_UPDATE, Map.of("status", order.getStatus()));
        }

        mapper.toEntity(r, order);

        /*
        Map includes old items
        Map<VariantId, OrderItem>
         */
        Map<Long, OrderItem> existingItemsMap = order.getOrderItems().stream()
                .collect(Collectors.toMap(item -> item.getVariant().getId(), item -> item));

        for (OrderItemRequest reqItem : r.getItems()) {
            // Order must be at least 1 product
            if (reqItem.getQuantity() < 1) {
                throw new BusinessException(ErrorCode.ORDER_INVALID_QUANTITY);
            }

            OrderItem existingItem = existingItemsMap.get(reqItem.getVariantId());

            // Update old item
            if (existingItem != null) {
                int quantityDiff = reqItem.getQuantity() - existingItem.getQuantity();
                if (quantityDiff > 0) {
                    int updatedRows = variantService.deductStock(reqItem.getVariantId(), reqItem.getQuantity());
                    if (updatedRows == 0) {
                        throw new BusinessException(
                                ErrorCode.PRODUCT_OUT_OF_STOCK,
                                Map.of("variantId", reqItem.getVariantId())
                        );
                    }
                } else if (quantityDiff < 0) {
                    variantService.addStock(reqItem.getVariantId(), Math.abs(quantityDiff));
                }

                existingItem.setQuantity(reqItem.getQuantity());
                existingItemsMap.remove(reqItem.getVariantId());

            // Insert new item
            } else {
                int updateRows = variantService.deductStock(reqItem.getVariantId(), reqItem.getQuantity());
                if (updateRows == 0) {
                    throw new BusinessException(
                            ErrorCode.PRODUCT_OUT_OF_STOCK,
                            Map.of("variantId", reqItem.getVariantId())
                    );
                }
                ProductVariant variant = variantService.findById(reqItem.getVariantId());
                OrderItem newItem = new OrderItem();
                newItem.setVariant(variant);
                newItem.setOrder(order);
                newItem.setQuantity(reqItem.getQuantity());
                newItem.setPrice(variant.getPrice());

                order.getOrderItems().add(newItem);
            }


        }
        List<OrderItem> removedItems = new ArrayList<>();
        for (OrderItem removedItem : existingItemsMap.values()) {
            variantService.addStock(removedItem.getVariant().getId(), removedItem.getQuantity());
            order.getOrderItems().remove(removedItem);
            removedItems.add(removedItem);
        }

        itemService.deleteAll(removedItems);

        order.setTotalPrice(calcTotalPrice(order));

        return mapper.toResponse(repository.save(order));
    }

    public Order findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
    }

    private BigDecimal calcTotalPrice(Order order) {
        return order.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
