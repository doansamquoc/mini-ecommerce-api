package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.VariantCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.entity.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
	componentModel = "spring",
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface VariantMapper {
	Variant toEntity(VariantRequest request);

	Variant toEntity(VariantCreationRequest request);

	void toUpdate(VariantRequest request, @MappingTarget Variant variant);

	void toUpdate(VariantUpdateRequest request, @MappingTarget Variant variant);

	VariantResponse toResponse(Variant variant);

	List<VariantResponse> toResponseList(List<Variant> variants);
}
