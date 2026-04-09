package com.sam.miniecommerceapi.cart.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record CartResponse(
        Long id,
        Long userId,
        Set<CartItemResponse> items,
        Integer totalItems,
        BigDecimal totalPrice
) {}
