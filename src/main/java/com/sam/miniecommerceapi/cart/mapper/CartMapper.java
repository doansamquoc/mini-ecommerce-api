package com.sam.miniecommerceapi.cart.mapper;

import com.sam.miniecommerceapi.cart.dto.request.CartCreationRequest;
import com.sam.miniecommerceapi.cart.dto.response.CartResponse;
import com.sam.miniecommerceapi.cart.entity.Cart;
import com.sam.miniecommerceapi.product.mapper.ProductVariantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = {ProductVariantMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CartMapper {
    Cart toEntity(CartCreationRequest r);

    @Mapping(source = "variant", target = "variant")
    CartResponse toResponse(Cart cart);
}
