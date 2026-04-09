package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.AttributeDefinitionCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.AttributeDefinitionUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeDefinitionResponse;
import com.sam.miniecommerceapi.product.entity.AttributeDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AttributeDefinitionMapper {
    AttributeDefinition toEntity(AttributeDefinitionCreationRequest request);

    @Mapping(source = "category.id", target = "categoryId")
    AttributeDefinitionResponse toResponse(AttributeDefinition attributeDefinition);

    void toUpdate(@MappingTarget AttributeDefinition attributeDefinition, AttributeDefinitionUpdateRequest request);
}
