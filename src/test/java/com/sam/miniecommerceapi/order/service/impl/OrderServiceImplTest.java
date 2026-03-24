package com.sam.miniecommerceapi.order.service.impl;

import com.sam.miniecommerceapi.common.enums.OrderStatus;
import com.sam.miniecommerceapi.common.enums.PaymentMethod;
import com.sam.miniecommerceapi.order.dto.request.OrderItemRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderRequest;
import com.sam.miniecommerceapi.order.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class OrderServiceImplTest {
    @Mock
    OrderRepository repository;

    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    void createOrder_ShouldReturnResponse_WhenDataIsValid() {
        Long userId = 1L;

        OrderRequest request = new OrderRequest();
        request.setStatus(OrderStatus.PENDING);
        request.setPaymentMethod(PaymentMethod.COD);
        request.setShippingAddress("Ha Noi, Viet Nam");
        request.setPhoneNumber("0877242416");
        request.setItems(List.of(
                new OrderItemRequest(1L, 5),
                new OrderItemRequest(2L, 10),
                new OrderItemRequest(3L, 15)));


    }
}