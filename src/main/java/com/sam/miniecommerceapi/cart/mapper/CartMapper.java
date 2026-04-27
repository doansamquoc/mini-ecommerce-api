package com.sam.miniecommerceapi.cart.mapper;

import com.sam.miniecommerceapi.cart.dto.request.CartCreationRequest;
import com.sam.miniecommerceapi.cart.dto.response.CartResponse;
import com.sam.miniecommerceapi.cart.entity.Cart;
import com.sam.miniecommerceapi.product.mapper.ProductVariantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
	componentModel = "spring",
	uses = {ProductVariantMapper.class},
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CartMapper {
	Cart toEntity(CartCreationRequest r);

	CartResponse toResponse(Cart cart);
}
