package com.sam.miniecommerceapi.order.mapper;

import com.sam.miniecommerceapi.order.dto.request.OrderItemRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderItemResponse;
import com.sam.miniecommerceapi.order.entity.OrderItem;
import com.sam.miniecommerceapi.product.mapper.ProductVariantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        uses = {ProductVariantMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface OrderItemMapper {
    OrderItem toEntity(OrderItemRequest r);

    @Mapping(target = "variant", source = "variant")
    OrderItemResponse toResponse(OrderItem item);
}
