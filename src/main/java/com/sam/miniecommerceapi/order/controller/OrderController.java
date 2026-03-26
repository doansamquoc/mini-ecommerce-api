package com.sam.miniecommerceapi.order.controller;

import com.sam.miniecommerceapi.common.annotation.CurrentUserId;
import com.sam.miniecommerceapi.common.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.common.dto.response.api.factory.ApiFactory;
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
}
