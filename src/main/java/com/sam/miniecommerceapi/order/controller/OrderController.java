package com.sam.miniecommerceapi.order.controller;

import com.sam.miniecommerceapi.common.annotation.CurrentUserId;
import com.sam.miniecommerceapi.common.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.common.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.order.dto.request.CancelOrderRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderCreationRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
import com.sam.miniecommerceapi.order.service.OrderCancellationService;
import com.sam.miniecommerceapi.order.service.OrderDeliveryService;
import com.sam.miniecommerceapi.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name = "Order endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;
    OrderDeliveryService deliveryService;
    OrderCancellationService cancellationService;

    @PostMapping
    @Operation(summary = "Create order")
    ResponseEntity<SuccessApi<OrderResponse>> createOrder(
            @CurrentUserId Long userId,
            @Valid @RequestBody OrderCreationRequest r
    ) {
        OrderResponse response = orderService.createOrder(userId, r);
        return ApiFactory.success(response, "Order created successfully.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order")
    ResponseEntity<SuccessApi<OrderResponse>> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderCreationRequest r
    ) {
        OrderResponse response = orderService.updateOrder(id, r);
        return ApiFactory.success(response, "Order updated successfully.");
    }

    @PostMapping("/cancel/{id}")
    @Operation(summary = "Cancel order")
    ResponseEntity<SuccessApi<OrderResponse>> cancelOrder(
            @PathVariable Long id,
            @RequestBody CancelOrderRequest request
    ) {
        OrderResponse response = cancellationService.cancelOrder(id, request);
        return ApiFactory.success(response, "Order has been canceled");
    }

    @PutMapping("/delivery/{id}")
    @Operation(summary = "Delivery order")
    ResponseEntity<SuccessApi<OrderResponse>> deliveryOrder(@PathVariable Long id) {
        OrderResponse response = deliveryService.deliveryOrder(id);
        return ApiFactory.success(response, "Order are delivering");
    }
}
