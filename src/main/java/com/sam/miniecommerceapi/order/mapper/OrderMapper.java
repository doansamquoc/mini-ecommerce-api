package com.sam.miniecommerceapi.order.mapper;

import com.sam.miniecommerceapi.order.dto.request.OrderRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
import com.sam.miniecommerceapi.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.lang.annotation.Target;

@Mapper(
        componentModel = "spring",
        uses = {OrderItemMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {
    Order toEntity(OrderRequest r);
    Order toEntity(OrderRequest r, @MappingTarget Order order);

    @Mapping(target = "items", source = "orderItems")
    OrderResponse toResponse(Order order);
}
