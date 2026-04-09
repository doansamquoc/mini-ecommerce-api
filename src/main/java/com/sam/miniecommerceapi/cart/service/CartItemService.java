package com.sam.miniecommerceapi.cart.service;

import com.sam.miniecommerceapi.cart.dto.request.CartItemAdditionRequest;
import com.sam.miniecommerceapi.cart.dto.request.CartItemUpdateRequest;
import com.sam.miniecommerceapi.cart.dto.response.CartItemResponse;

import java.util.List;

public interface CartItemService {
    CartItemResponse addToCart(Long userId, CartItemAdditionRequest request);

    CartItemResponse updateCartItem(Long userId, CartItemUpdateRequest request);

    List<CartItemResponse> getAllItems(Long userId);
}
