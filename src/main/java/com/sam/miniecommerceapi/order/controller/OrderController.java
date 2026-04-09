package com.sam.miniecommerceapi.order.controller;

import com.sam.miniecommerceapi.common.annotation.CurrentUserId;
import com.sam.miniecommerceapi.common.constant.AppConstant;
import com.sam.miniecommerceapi.common.constant.OrderStatus;
import com.sam.miniecommerceapi.common.dto.response.ApiResponse;
import com.sam.miniecommerceapi.common.dto.response.PageResponse;
import com.sam.miniecommerceapi.common.util.SortUtils;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
	ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getOrders(
		@RequestParam(value = "status", required = false) OrderStatus status,
		@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
		@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
		@RequestParam(value = "sort", defaultValue = "orderDate, desc") String sort
	) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, SortUtils.extractSortFromString(sort));
		PageResponse<OrderResponse> responses = orderService.getOrderByStatus(status, pageable);
		return ResponseEntity.ok(ApiResponse.of(responses));
	}

	@GetMapping("/me")
	@Operation(summary = "Get orders with pagination and status by me")
	ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> myOrders(
		@CurrentUserId Long userId,
		@RequestParam(value = "status", required = false) OrderStatus status,
		@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
		@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
		@RequestParam(value = "sort", defaultValue = "orderDate, desc") String sort
	) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, SortUtils.extractSortFromString(sort));
		PageResponse<OrderResponse> responses = orderService.getOrderByUserId(userId, status, pageable);
		return ResponseEntity.ok(ApiResponse.of(responses));
	}


	@PostMapping
	@Operation(summary = "Create order")
	ResponseEntity<ApiResponse<OrderResponse>> createOrder(
		@CurrentUserId Long userId,
		@Valid @RequestBody OrderRequest request
	) {
		OrderResponse response = orderService.createOrder(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}

	@PatchMapping("/{orderId}")
	@Operation(summary = "Update order fields")
	@PreAuthorize(AppConstant.MANAGER_OR_ADMIN)
	ResponseEntity<ApiResponse<OrderResponse>> updateOrder(
		@PathVariable Long orderId,
		@Valid @RequestBody OrderRequest request
	) {
		OrderResponse response = orderService.updateOrder(orderId, request);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PatchMapping("/{orderId}/cancel")
	@Operation(summary = "Cancel order with reason")
	ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
		@PathVariable Long orderId,
		@RequestBody CancelOrderRequest request
	) {
		OrderResponse response = orderService.cancelOrder(orderId, request);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PatchMapping("/{orderId}/delivered")
	@Operation(summary = "Mark as order delivered")
	ResponseEntity<ApiResponse<OrderResponse>> markAsDelivered(@PathVariable Long orderId) {
		OrderResponse response = orderService.deliveredOrder(orderId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PatchMapping("/{orderId}/confirm")
	@Operation(summary = "Mark as order confirm", description = "Admin or Manager will confirm an order from customer")
	ResponseEntity<ApiResponse<OrderResponse>> markAsConfirm(@PathVariable Long orderId) {
		OrderResponse response = orderService.confirmOrder(orderId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PatchMapping("/{orderId}/delivering")
	@Operation(summary = "Mark as order delivering")
	ResponseEntity<ApiResponse<OrderResponse>> markAsDelivering(@PathVariable Long orderId) {
		OrderResponse response = orderService.deliveringOrder(orderId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PatchMapping("/{orderId}/pending-payment")
	@Operation(summary = "Mark as pending payment")
	ResponseEntity<ApiResponse<OrderResponse>> markAsPendingPayment(@PathVariable Long orderId) {
		OrderResponse response = orderService.paymentPendingOrder(orderId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PatchMapping("/{orderId}/payment-paid")
	@Operation(summary = "Mark as paid")
	ResponseEntity<ApiResponse<OrderResponse>> markAsPaid(@PathVariable Long orderId) {
		OrderResponse response = orderService.paymentPaidOrder(orderId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PatchMapping("/{orderId}/payment-failed")
	@Operation(summary = "Mark as payment failed")
	ResponseEntity<ApiResponse<OrderResponse>> markAsPaymentFailed(@PathVariable Long orderId) {
		OrderResponse response = orderService.paymentFailedOrder(orderId);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
