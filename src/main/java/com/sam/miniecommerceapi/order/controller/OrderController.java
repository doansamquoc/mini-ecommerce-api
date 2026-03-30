package com.sam.miniecommerceapi.order.controller;

import com.sam.miniecommerceapi.order.dto.request.CancelOrderRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
import com.sam.miniecommerceapi.order.service.OrderService;
import com.sam.miniecommerceapi.shared.annotation.CurrentUserId;
import com.sam.miniecommerceapi.shared.constant.AppConstant;
import com.sam.miniecommerceapi.shared.constant.OrderStatus;
import com.sam.miniecommerceapi.shared.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.shared.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.shared.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.shared.util.SortUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name = "Order endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @GetMapping
    @Operation(summary = "Get orders with pagination and status")
    ResponseEntity<SuccessApi<PageResponse<OrderResponse>>> getOrders(
            @RequestParam(value = "status", required = false) OrderStatus status,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sort", defaultValue = "orderDate, desc") String sort
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, SortUtils.extractSortFromString(sort));

        PageResponse<OrderResponse> responses = orderService.getOrderByStatus(status, pageable);
        return ApiFactory.success(responses, "Get order(s) by " + status + " successfully");
    }

    @GetMapping("/me")
    @Operation(summary = "Get orders with pagination and status by me")
    ResponseEntity<SuccessApi<PageResponse<OrderResponse>>> myOrders(
            @CurrentUserId Long userId,
            @RequestParam(value = "status", required = false) OrderStatus status,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sort", defaultValue = "orderDate, desc") String sort
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, SortUtils.extractSortFromString(sort));
        PageResponse<OrderResponse> responses = orderService.getOrderByUserId(userId, status, pageable);
        return ApiFactory.success(responses, "Get order(s) by user " + userId + " successfully");
    }


    @PostMapping
    @Operation(summary = "Create order")
    ResponseEntity<SuccessApi<OrderResponse>> createOrder(
            @CurrentUserId Long userId,
            @Valid @RequestBody OrderRequest request
    ) {
        OrderResponse response = orderService.createOrder(userId, request);
        return ApiFactory.success(response, "Order created successfully");
    }

    @PatchMapping("/{orderId}")
    @Operation(summary = "Update order fields")
    @PreAuthorize(AppConstant.MANAGER_OR_ADMIN)
    ResponseEntity<SuccessApi<OrderResponse>> updateOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderRequest request
    ) {
        OrderResponse response = orderService.updateOrder(orderId, request);
        return ApiFactory.success(response, "Order updated successfully");
    }

    @PatchMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel order with reason")
    ResponseEntity<SuccessApi<OrderResponse>> cancelOrder(
            @PathVariable Long orderId,
            @RequestBody CancelOrderRequest request
    ) {
        OrderResponse response = orderService.cancelOrder(orderId, request);
        return ApiFactory.success(response, "Order has been canceled");
    }

    @PatchMapping("/{orderId}/delivered")
    @Operation(summary = "Mark as order delivered")
    ResponseEntity<SuccessApi<OrderResponse>> markAsDelivered(@PathVariable Long orderId) {
        OrderResponse response = orderService.deliveredOrder(orderId);
        return ApiFactory.success(response, "Order is being delivered");
    }

    @PatchMapping("/{orderId}/confirm")
    @Operation(summary = "Mark as order confirm", description = "Admin or Manager will confirm an order from customer")
    ResponseEntity<SuccessApi<OrderResponse>> markAsConfirm(@PathVariable Long orderId) {
        OrderResponse response = orderService.confirmOrder(orderId);
        return ApiFactory.success(response, "Order has been confirmed");
    }

    @PatchMapping("/{orderId}/delivering")
    @Operation(summary = "Mark as order delivering")
    ResponseEntity<SuccessApi<OrderResponse>> markAsDelivering(@PathVariable Long orderId) {
        OrderResponse response = orderService.deliveringOrder(orderId);
        return ApiFactory.success(response, "Order is delivering");
    }

    @PatchMapping("/{orderId}/pending-payment")
    @Operation(summary = "Mark as pending payment")
    ResponseEntity<SuccessApi<OrderResponse>> markAsPendingPayment(@PathVariable Long orderId) {
        OrderResponse response = orderService.paymentPendingOrder(orderId);
        return ApiFactory.success(response, "Order is pending payment");
    }

    @PatchMapping("/{orderId}/payment-paid")
    @Operation(summary = "Mark as paid")
    ResponseEntity<SuccessApi<OrderResponse>> markAsPaid(@PathVariable Long orderId) {
        OrderResponse response = orderService.paymentPaidOrder(orderId);
        return ApiFactory.success(response, "Order has been paid");
    }

    @PatchMapping("/{orderId}/payment-failed")
    @Operation(summary = "Mark as payment failed")
    ResponseEntity<SuccessApi<OrderResponse>> markAsPaymentFailed(@PathVariable Long orderId) {
        OrderResponse response = orderService.paymentFailedOrder(orderId);
        return ApiFactory.success(response, "Order payment failed");
    }
}
