package com.sam.miniecommerceapi.cart.service;

import com.sam.miniecommerceapi.cart.dto.response.CartResponse;
import com.sam.miniecommerceapi.cart.entity.Cart;
import org.springframework.data.domain.Pageable;

public interface CartService {
    Cart createCart(Long userId);

    Cart getAllOrCreateCart(Long userId);

    Cart getOrCreateCart(Long userId);

    CartResponse getCart(Long userId, Pageable pageable);
}
