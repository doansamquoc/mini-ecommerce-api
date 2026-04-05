package com.sam.miniecommerceapi.cart.controller;

import com.sam.miniecommerceapi.cart.dto.request.CartCreationRequest;
import com.sam.miniecommerceapi.cart.dto.response.CartResponse;
import com.sam.miniecommerceapi.cart.service.CartService;
import com.sam.miniecommerceapi.shared.annotation.CurrentUserId;
import com.sam.miniecommerceapi.shared.dto.response.ApiResponse;
import com.sam.miniecommerceapi.shared.dto.response.PageResponse;
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
@Tag(name = "Cart endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @Operation(summary = "Add product to cart")
    @PostMapping
    ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @CurrentUserId Long userId,
            @Valid @RequestBody CartCreationRequest r
    ) {
        CartResponse response = cartService.addToCart(userId, r);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @Operation(summary = "Get all products in cart by cart ID")
    @GetMapping
    ResponseEntity<ApiResponse<PageResponse<CartResponse>>> getProductsInCart(
            @CurrentUserId Long id,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize
    ) {
        PageResponse<CartResponse> responses = cartService.getCarts(pageNumber, pageSize, id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(responses));
    }

    @Operation(summary = "Delete product in cart")
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<String>> deleteCart(@PathVariable Long id, @CurrentUserId Long userId) {
        cartService.deleteCart(userId, id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of());
    }

    @Operation(summary = "Delete all products in cart")
    @DeleteMapping
    ResponseEntity<ApiResponse<String>> deleteAllCartsByUserId(@CurrentUserId Long userId) {
        cartService.deleteAllByUser(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of());
    }
}
