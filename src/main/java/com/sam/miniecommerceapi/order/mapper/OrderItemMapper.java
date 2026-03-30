package com.sam.miniecommerceapi.order.mapper;

import com.sam.miniecommerceapi.order.dto.request.OrderItemRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderItemResponse;
import com.sam.miniecommerceapi.order.entity.OrderItem;
import com.sam.miniecommerceapi.product.mapper.ProductVariantMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductVariantMapper.class})
public interface OrderItemMapper {
    OrderItem toEntity(OrderItemRequest request);

    OrderItemResponse toResponse(OrderItem item);
}
