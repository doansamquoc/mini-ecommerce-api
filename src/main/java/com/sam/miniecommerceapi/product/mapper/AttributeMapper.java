package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.AttributeCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.AttributeUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeResponse;
import com.sam.miniecommerceapi.product.entity.Attribute;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AttributeMapper {
    Attribute toEntity(AttributeCreationRequest request);

    AttributeResponse toResponse(Attribute attribute);

    List<AttributeResponse> toResponseList(List<Attribute> attributes);

    void toUpdate(@MappingTarget Attribute attribute, AttributeUpdateRequest request);
}
