package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.AttributeTermCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.AttributeTermUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeResponse;
import com.sam.miniecommerceapi.product.dto.response.AttributeTermResponse;
import com.sam.miniecommerceapi.product.entity.Attribute;
import com.sam.miniecommerceapi.product.entity.AttributeTerm;
import com.sam.miniecommerceapi.product.entity.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.*;

@Mapper(componentModel = "spring")
public interface AttributeTermMapper {
    AttributeTerm toEntity(AttributeTermCreationRequest request);

    AttributeTermResponse toResponse(AttributeTerm attributeTerm);

    List<AttributeTermResponse> toResponseList(List<AttributeTerm> attributeTerms);

    void toUpdate(@MappingTarget AttributeTerm attributeTerm, AttributeTermUpdateRequest request);
}
