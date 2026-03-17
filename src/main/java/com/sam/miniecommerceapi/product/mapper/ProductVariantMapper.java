package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.ProductVariantRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductAttributeOptionResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductVariantResponse;
import com.sam.miniecommerceapi.product.entity.AttributeOption;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductVariantMapper {
    ProductVariant toEntity(ProductVariantRequest r);

    ProductVariantResponse toResponse(ProductVariant productVariant);

    List<ProductVariantResponse> toResponses(List<ProductVariant> variants);

    @Mapping(source = "attribute.name", target = "attribute")
    ProductAttributeOptionResponse mapOption(AttributeOption option);
}
