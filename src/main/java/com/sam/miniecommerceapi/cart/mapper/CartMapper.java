package com.sam.miniecommerceapi.cart.mapper;

import com.sam.miniecommerceapi.cart.dto.request.CartCreationRequest;
import com.sam.miniecommerceapi.cart.dto.response.CartResponse;
import com.sam.miniecommerceapi.cart.entity.Cart;
import com.sam.miniecommerceapi.product.mapper.VariantMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {VariantMapper.class})
public interface CartMapper {
    Cart toEntity(CartCreationRequest r);

    CartResponse toResponse(Cart cart);
}
