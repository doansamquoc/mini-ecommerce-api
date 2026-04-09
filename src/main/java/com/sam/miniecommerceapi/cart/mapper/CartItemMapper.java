package com.sam.miniecommerceapi.cart.mapper;

import com.sam.miniecommerceapi.cart.dto.request.CartItemAdditionRequest;
import com.sam.miniecommerceapi.cart.dto.response.CartItemResponse;
import com.sam.miniecommerceapi.cart.entity.CartItem;
import org.hibernate.Hibernate;
import org.mapstruct.*;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CartItemMapper {
	@Condition
	default boolean isInitialized(Object object) {
		return Hibernate.isInitialized(object);
	}

	CartItem toEntity(CartItemAdditionRequest request);

	@Mapping(source = "variant.id", target = "variantId")
	@Mapping(source = "variant.product.name", target = "productName")
	@Mapping(source = "variant.sku", target = "sku")
	@Mapping(source = "variant.imageUrl", target = "imageUrl")
	@Mapping(source = "variant.price", target = "price")
	@Mapping(target = "subTotal", expression = "java(cartItem.getSubTotal())")
	CartItemResponse toResponse(CartItem cartItem);
}
