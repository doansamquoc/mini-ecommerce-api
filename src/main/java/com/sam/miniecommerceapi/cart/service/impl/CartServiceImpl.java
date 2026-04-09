package com.sam.miniecommerceapi.cart.service.impl;

import com.sam.miniecommerceapi.cart.dto.response.CartItemResponse;
import com.sam.miniecommerceapi.cart.dto.response.CartResponse;
import com.sam.miniecommerceapi.cart.entity.Cart;
import com.sam.miniecommerceapi.cart.mapper.CartItemMapper;
import com.sam.miniecommerceapi.cart.mapper.CartMapper;
import com.sam.miniecommerceapi.cart.repository.CartRepository;
import com.sam.miniecommerceapi.cart.service.CartService;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    UserService userService;
    CartItemMapper itemMapper;
    CartRepository repository;

    @Override
    public Cart createCart(Long userId) {
        User user = userService.getReference(userId);
        Cart cart = new Cart();
        cart.setUser(user);
        return save(cart);
    }

    @Override
    public Cart getAllOrCreateCart(Long userId) {
        return repository.findFullByUserId(userId).orElseGet(() -> createCart(userId));
    }

    @Override
    public Cart getOrCreateCart(Long userId) {
        return repository.findFullByUserId(userId).orElseGet(() -> createCart(userId));
    }

    @Override
    public CartResponse getCart(Long userId, Pageable pageable) {
        Cart cart = getAllOrCreateCart(userId);
        Set<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(itemMapper::toResponse)
                .collect(Collectors.toSet());
        return new CartResponse(cart.getId(), userId, itemResponses, cart.getTotalItems(), cart.getTotalPrice());
    }

    public boolean existsByUserId(Long userId) {
        return repository.existsByUserId(userId);
    }

    private Cart save(Cart cart) {
        return repository.save(cart);
    }

    @Transactional
    public void deleteCart(Long userId, Long cartId) {
        repository.deleteCart(cartId, userId);
    }

    @Transactional
    public void deleteAllByUser(Long userId) {
        repository.deleteCartByUserId(userId);
    }
}
