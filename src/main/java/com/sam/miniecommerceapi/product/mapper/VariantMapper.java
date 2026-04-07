package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.VariantRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeWithTermResponse;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.entity.AttributeTerm;
import com.sam.miniecommerceapi.product.entity.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {AttributeTermMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface VariantMapper {
    Variant toEntity(VariantRequest request);

    @Mapping(source = "name", target = "option")
    @Mapping(source = "attribute.name", target = "name")
    AttributeWithTermResponse toResponse(AttributeTerm attributeTerm);

    void toUpdate(VariantRequest request, @MappingTarget Variant variant);

    void toUpdate(VariantUpdateRequest request, @MappingTarget Variant variant);

    @Mapping(source = "variantAttributes", target = "attributes")
    VariantResponse toResponse(Variant variant);

    List<VariantResponse> toResponseList(List<Variant> variants);
}
