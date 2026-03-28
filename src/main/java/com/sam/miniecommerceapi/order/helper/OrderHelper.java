package com.sam.miniecommerceapi.order.helper;

import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.order.entity.Order;
import com.sam.miniecommerceapi.order.entity.OrderItem;
import com.sam.miniecommerceapi.order.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderHelper {
    OrderRepository repository;

    public Order findById(Long orderId) {
        return repository.findById(orderId).orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    }

    public Order save(Order order) {
        return repository.save(order);
    }
}
