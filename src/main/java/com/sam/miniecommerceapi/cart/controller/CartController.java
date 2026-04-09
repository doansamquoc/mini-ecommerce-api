package com.sam.miniecommerceapi.cart.controller;

import com.sam.miniecommerceapi.cart.dto.request.CartItemAdditionRequest;
import com.sam.miniecommerceapi.cart.dto.request.CartItemUpdateRequest;
import com.sam.miniecommerceapi.cart.dto.response.CartItemResponse;
import com.sam.miniecommerceapi.cart.dto.response.CartResponse;
import com.sam.miniecommerceapi.cart.service.CartItemService;
import com.sam.miniecommerceapi.cart.service.CartService;
import com.sam.miniecommerceapi.common.annotation.CurrentUserId;
import com.sam.miniecommerceapi.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
@Tag(name = "Cart Endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
	CartService service;
	CartItemService itemService;

	@Operation(summary = "Add a product variation to cart.")
	@PostMapping
	ResponseEntity<ApiResponse<CartItemResponse>> addToCart(
		@CurrentUserId Long userId,
		@Valid @RequestBody CartItemAdditionRequest request
	) {
		CartItemResponse response = itemService.addToCart(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}

	@Operation(summary = "Get all products in cart by current user")
	@GetMapping
	ResponseEntity<ApiResponse<CartResponse>> getProductsInCart(
		@CurrentUserId Long userId,
		@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
		@RequestParam(name = "pageSize", defaultValue = "20") int pageSize
	) {
		CartResponse responses = service.getCart(userId, null);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(responses));
	}

	@Operation(summary = "Update a cart item.")
	@PatchMapping
	ResponseEntity<ApiResponse<CartItemResponse>> updateItem(
		@CurrentUserId Long userId,
		@Valid @RequestBody CartItemUpdateRequest request
	) {
		CartItemResponse response = itemService.updateCartItem(userId, request);
		return ResponseEntity.ok(ApiResponse.of(response));
	}
}
