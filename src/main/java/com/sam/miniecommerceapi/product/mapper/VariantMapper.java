package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.VariantRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.entity.AttributeValue;
import com.sam.miniecommerceapi.product.entity.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = {AttributeOptionMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface VariantMapper {
    @Mapping(target = "values", source = "attributeValueIds")
    Variant toEntity(VariantRequest request);

    default AttributeValue mapIdToAttributeValue(Long id) {
        if (id == null) return null;
        AttributeValue av = new AttributeValue();
        av.setId(id); // Chỉ cần ID để Hibernate lưu vào bảng join
        return av;
    }

    Variant toEntity(VariantUpdateRequest request);

    Variant toEntity(VariantUpdateRequest request, @MappingTarget Variant variant);

    @Mapping(target = "id", ignore = true)
    void updateVariant(VariantUpdateRequest request, @MappingTarget Variant variant);

    VariantResponse toDto(Variant variant);
}
