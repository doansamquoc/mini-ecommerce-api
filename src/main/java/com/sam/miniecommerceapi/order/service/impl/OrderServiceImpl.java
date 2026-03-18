package com.sam.miniecommerceapi.order.service.impl;

import com.sam.miniecommerceapi.common.enums.ErrorCode;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        order.setTotalPrice(BigDecimal.ZERO);

        BigDecimal totalOrderPrice = BigDecimal.ZERO;

        for (OrderItemRequest i : r.getItems()) {
            ProductVariant variant = variantService.findById(i.getVariantId());
            // ... (check stock logic)

            variant.setStockQuantity(variant.getStockQuantity() - i.getQuantity());
            variantService.save(variant);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .variant(variant)
                    .quantity(i.getQuantity())
                    .price(variant.getPrice())
                    .build();

            order.getOrderItems().add(orderItem);
            totalOrderPrice = totalOrderPrice.add(variant.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())));
        }
        order.setTotalPrice(totalOrderPrice);

        return mapper.toResponse(repository.save(order));
    }
}
