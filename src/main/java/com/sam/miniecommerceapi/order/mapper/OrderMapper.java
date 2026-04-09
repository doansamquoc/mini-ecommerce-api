package com.sam.miniecommerceapi.order.mapper;

import com.sam.miniecommerceapi.order.dto.request.CancelOrderRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
import com.sam.miniecommerceapi.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
	componentModel = "spring",
	uses = {OrderItemMapper.class},
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {
	Order toEntity(OrderRequest r);

	Order toEntity(OrderRequest r, @MappingTarget Order order);

	Order toEntity(CancelOrderRequest request, @MappingTarget Order oder);

	OrderResponse toResponse(Order order);
}
