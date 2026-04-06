package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.VariantRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantUpdateRequest;
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
    @Mapping(target = "values", source = "attributeValueIds")
    Variant toUpdate(VariantRequest request);

    default AttributeTerm mapIdToAttributeValue(Long id) {
        if (id == null) return null;
        AttributeTerm av = new AttributeTerm();
        av.setId(id); // Chỉ cần ID để Hibernate lưu vào bảng join
        return av;
    }

    Variant toUpdate(VariantUpdateRequest request);

    void toUpdate(VariantRequest request, @MappingTarget Variant variant);

    @Mapping(target = "id", ignore = true)
    void updateVariant(VariantUpdateRequest request, @MappingTarget Variant variant);

    VariantResponse toResponse(Variant variant);
    List<VariantResponse> toResponseList(List<Variant> variants);
}
