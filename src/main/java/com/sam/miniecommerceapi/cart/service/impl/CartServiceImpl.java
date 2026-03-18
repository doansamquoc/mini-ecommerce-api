package com.sam.miniecommerceapi.cart.service.impl;

import com.sam.miniecommerceapi.cart.dto.request.CartCreationRequest;
import com.sam.miniecommerceapi.cart.dto.response.CartResponse;
import com.sam.miniecommerceapi.cart.entity.Cart;
import com.sam.miniecommerceapi.cart.mapper.CartMapper;
import com.sam.miniecommerceapi.cart.repository.CartRepository;
import com.sam.miniecommerceapi.cart.service.CartService;
import com.sam.miniecommerceapi.common.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.product.service.ProductVariantService;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartMapper mapper;
    UserService userService;
    CartRepository repository;
    ProductVariantService variantService;

    @Override
    @Transactional
    public CartResponse addToCart(Long userId, CartCreationRequest r) {
        User user = userService.findById(userId);
        ProductVariant variant = variantService.findById(r.getProductVariantId());

        if (variant.getStockQuantity() - r.getQuantity() < 0)
            throw new BusinessException(ErrorCode.PRODUCT_VARIANT_NOT_ENOUGH);


        Cart cart = repository.findByUserAndVariant(user, variant)
                .orElseGet(() -> Cart.builder().user(user).variant(variant).quantity(0).build());

        cart.setQuantity(cart.getQuantity() + r.getQuantity());

        if (variant.getStockQuantity() - cart.getQuantity() < 0)
            throw new BusinessException(ErrorCode.PRODUCT_VARIANT_NOT_ENOUGH);

        return mapper.toResponse(repository.save(cart));
    }

    /**
     * Get all products in cart by user ID
     *
     * @param pageNumber Page number
     * @param pageSize   Page size
     * @param id         User ID
     * @return List carts
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<CartResponse> getCarts(int pageNumber, int pageSize, Long id) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending());
        Page<Cart> carts = repository.findAllByUserId(id, pageable);

        if (carts.isEmpty()) return PageResponse.from(Page.empty());

        Page<CartResponse> cartResponses = carts.map(mapper::toResponse);

        return PageResponse.from(cartResponses);
    }

    @Override
    @Transactional
    public void deleteCart(Long userId, Long cartId) {
        repository.deleteCart(cartId, userId);
    }

    @Transactional
    @Override
    public void deleteAllByUser(Long userId) {
        repository.deleteCartByUserId(userId);
    }
}
