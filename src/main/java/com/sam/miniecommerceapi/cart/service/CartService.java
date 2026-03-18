package com.sam.miniecommerceapi.cart.service;

import com.sam.miniecommerceapi.cart.dto.request.CartCreationRequest;
import com.sam.miniecommerceapi.cart.dto.response.CartResponse;
import com.sam.miniecommerceapi.common.dto.response.pagination.PageResponse;

public interface CartService {
    CartResponse addToCart(Long id, CartCreationRequest r);

    PageResponse<CartResponse> getCarts(int pageNumber, int PageSize, Long id);

    void deleteCart(Long userId, Long cartId);
}
