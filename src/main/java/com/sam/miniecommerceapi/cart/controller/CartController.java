package com.sam.miniecommerceapi.cart.controller;

import com.sam.miniecommerceapi.cart.dto.request.CartCreationRequest;
import com.sam.miniecommerceapi.cart.dto.response.CartResponse;
import com.sam.miniecommerceapi.cart.service.CartService;
import com.sam.miniecommerceapi.common.annotation.CurrentUserId;
import com.sam.miniecommerceapi.common.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.common.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.common.dto.response.pagination.PageResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @PostMapping
    ResponseEntity<SuccessApi<CartResponse>> addToCart(@Valid @RequestBody CartCreationRequest r) {
        CartResponse response = cartService.addToCart(r);
        return ApiFactory.success(response, "Add to cart successfully.");
    }

    @GetMapping
    ResponseEntity<SuccessApi<PageResponse<CartResponse>>> getProductsInCart(
            @CurrentUserId Long id,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize
    ) {
        PageResponse<CartResponse> responses = cartService.getCarts(pageNumber, pageSize, id);
        return ApiFactory.success(responses, "Get products in cart successfully.");
    }
}
