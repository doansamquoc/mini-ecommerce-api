package com.sam.miniecommerceapi.order.mapper;

import com.sam.miniecommerceapi.order.dto.request.CancelOrderRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderCreationRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
import com.sam.miniecommerceapi.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = {OrderItemMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {
    Order toEntity(OrderCreationRequest r);

    Order toEntity(OrderCreationRequest r, @MappingTarget Order order);

    Order toEntity(CancelOrderRequest request, @MappingTarget Order oder);

    @Mapping(target = "items", source = "orderItems")
    OrderResponse toResponse(Order order);
}
