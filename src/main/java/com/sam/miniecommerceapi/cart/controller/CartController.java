package com.sam.miniecommerceapi.cart.controller;

import com.sam.miniecommerceapi.cart.dto.request.CartCreationRequest;
import com.sam.miniecommerceapi.cart.dto.response.CartResponse;
import com.sam.miniecommerceapi.cart.service.CartService;
import com.sam.miniecommerceapi.common.annotation.CurrentUserId;
import com.sam.miniecommerceapi.common.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.common.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.common.dto.response.pagination.PageResponse;
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
@RequestMapping("/api/v1/carts")
@Tag(name = "Cart endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @Operation(summary = "Add product to cart")
    @PostMapping
    ResponseEntity<SuccessApi<CartResponse>> addToCart(
            @CurrentUserId Long userId,
            @Valid @RequestBody CartCreationRequest r
    ) {
        CartResponse response = cartService.addToCart(userId, r);
        return ApiFactory.success(response, "Add to cart successfully.");
    }

    @Operation(summary = "Get all products in cart by cart ID")
    @GetMapping
    ResponseEntity<SuccessApi<PageResponse<CartResponse>>> getProductsInCart(
            @CurrentUserId Long id,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize
    ) {
        PageResponse<CartResponse> responses = cartService.getCarts(pageNumber, pageSize, id);
        return ApiFactory.success(responses, "Get products in cart successfully.");
    }

    @Operation(summary = "Delete product in cart")
    @DeleteMapping("/{id}")
    ResponseEntity<SuccessApi<String>> deleteCart(@PathVariable Long id, @CurrentUserId Long userId) {
        cartService.deleteCart(userId, id);
        return ApiFactory.success("Delete cart successfully.");
    }

    @Operation(summary = "Delete all products in cart")
    @DeleteMapping
    ResponseEntity<SuccessApi<String>> deleteAllCartsByUserId(@CurrentUserId Long userId) {
        cartService.deleteAllByUser(userId);
        return ApiFactory.success("Delete all carts successfully.");
    }
}
