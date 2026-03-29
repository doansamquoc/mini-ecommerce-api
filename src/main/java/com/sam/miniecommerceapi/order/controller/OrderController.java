package com.sam.miniecommerceapi.order.controller;

import com.sam.miniecommerceapi.common.annotation.CurrentUserId;
import com.sam.miniecommerceapi.common.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.common.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.order.dto.request.CancelOrderRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
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

    @PostMapping
    @Operation(summary = "Create order")
    ResponseEntity<SuccessApi<OrderResponse>> createOrder(
            @CurrentUserId Long userId,
            @Valid @RequestBody OrderRequest r
    ) {
        OrderResponse response = orderService.createOrder(userId, r);
        return ApiFactory.success(response, "Order created successfully.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order")
    ResponseEntity<SuccessApi<OrderResponse>> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequest r
    ) {
        OrderResponse response = orderService.updateOrder(id, r);
        return ApiFactory.success(response, "Order updated successfully.");
    }

    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel order")
    ResponseEntity<SuccessApi<OrderResponse>> cancelOrder(
            @PathVariable Long orderId,
            @RequestBody CancelOrderRequest request
    ) {
        OrderResponse response = orderService.cancelOrder(orderId, request);
        return ApiFactory.success(response, "Order has been canceled");
    }

    @PutMapping("/{orderId}/delivered")
    @Operation(summary = "Mark as order delivered")
    ResponseEntity<SuccessApi<OrderResponse>> deliveryOrder(@PathVariable Long orderId) {
        OrderResponse response = orderService.deliveredOrder(orderId);
        return ApiFactory.success(response, "Order is being delivered");
    }

    @PutMapping("/{orderId}/confirm")
    @Operation(summary = "Mark as order confirm", description = "Admin or Manager will confirm an order from customer")
    ResponseEntity<SuccessApi<OrderResponse>> confirmationOrder(@PathVariable Long orderId) {
        OrderResponse response = orderService.confirmOrder(orderId);
        return ApiFactory.success(response, "Order has been confirmed");
    }

    @PutMapping("/{orderId}/delivering")
    @Operation(summary = "Mark as order delivering")
    ResponseEntity<SuccessApi<OrderResponse>> deliveringOrder(@PathVariable Long orderId) {
        OrderResponse response = orderService.deliveringOrder(orderId);
        return ApiFactory.success(response, "Order is delivering");
    }

    @PutMapping("/{orderId}/pending-payment")
    @Operation(summary = "Mark as pending payment")
    ResponseEntity<SuccessApi<OrderResponse>> orderPendingPayment(@PathVariable Long orderId) {
        OrderResponse response = orderService.paymentPendingOrder(orderId);
        return ApiFactory.success(response, "Order is pending payment");
    }

    @PutMapping("/{orderId}/payment-paid")
    @Operation(summary = "Mark as paid")
    ResponseEntity<SuccessApi<OrderResponse>> orderPaid(@PathVariable Long orderId) {
        OrderResponse response = orderService.paymentPaidOrder(orderId);
        return ApiFactory.success(response, "Order has been paid");
    }

    @PutMapping("/{orderId}/payment-failed")
    @Operation(summary = "Mark as payment failed")
    ResponseEntity<SuccessApi<OrderResponse>> orderPaymentFailed(@PathVariable Long orderId) {
        OrderResponse response = orderService.paymentFailedOrder(orderId);
        return ApiFactory.success(response, "Order payment failed");
    }
}
