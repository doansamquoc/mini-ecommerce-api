package com.sam.miniecommerceapi.cart.service.impl;

import com.sam.miniecommerceapi.cart.dto.request.CartItemAdditionRequest;
import com.sam.miniecommerceapi.cart.dto.request.CartItemUpdateRequest;
import com.sam.miniecommerceapi.cart.dto.response.CartItemResponse;
import com.sam.miniecommerceapi.cart.entity.Cart;
import com.sam.miniecommerceapi.cart.entity.CartItem;
import com.sam.miniecommerceapi.cart.mapper.CartItemMapper;
import com.sam.miniecommerceapi.cart.repository.CartItemRepository;
import com.sam.miniecommerceapi.cart.service.CartItemService;
import com.sam.miniecommerceapi.cart.service.CartService;
import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.product.service.ProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemServiceImpl implements CartItemService {
	CartItemMapper mapper;
	CartService cartService;
	CartItemRepository repository;
	ProductVariantService productVariantService;

	@Override
	@Transactional
	public CartItemResponse addToCart(Long userId, CartItemAdditionRequest request) {
		CartItem cartItem = repository.findByUserIdAndVariantId(userId, request.variantId()).orElseGet(() -> {
			Cart cart = cartService.getOrCreateCart(userId);
			ProductVariant variant = productVariantService.findGraphById(request.variantId());
			return CartItem.builder().cart(cart).variant(variant).quantity(0).build();
		});

		int totalQuantity = cartItem.getQuantity() + request.quantity();
		if (cartItem.getVariant().getStock() < totalQuantity) {
			throw BusinessException.of(ErrorCode.PRODUCT_OUT_OF_STOCK);
		}

		cartItem.setQuantity(totalQuantity);
		return mapper.toResponse(save(cartItem));
	}

	@Override
	@Transactional
	public CartItemResponse updateCartItem(Long userId, CartItemUpdateRequest request) {
		Cart cart = cartService.getOrCreateCart(userId);
		CartItem cartItem = findByCartIdAndVariantId(cart.getId(), request.variantId());

		if (request.quantity() <= 0) {
			cart.getCartItems().remove(cartItem);
			return null;
		}

		ProductVariant variant = productVariantService.findById(request.variantId());
		if (variant.getStock() < request.quantity()) {
			throw BusinessException.of(ErrorCode.PRODUCT_OUT_OF_STOCK);
		}

		cartItem.setQuantity(request.quantity());
		return mapper.toResponse(save(cartItem));
	}

	@Override
	public List<CartItemResponse> getAllItems(Long userId) {
		Cart cart = cartService.getOrCreateCart(userId);
		return cart.getCartItems().stream().map(mapper::toResponse).toList();
	}

	private CartItem save(CartItem cartItem) {
		return repository.save(cartItem);
	}

	private CartItem findByCartIdAndVariantId(Long id, Long variantId) {
		return repository.findByUserIdAndVariantId(id, variantId).orElseThrow(
			() -> BusinessException.of(ErrorCode.PRODUCT_VARIANT_NOT_FOUND)
		);
	}
}
