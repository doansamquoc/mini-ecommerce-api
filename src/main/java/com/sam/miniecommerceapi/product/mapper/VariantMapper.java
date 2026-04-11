package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.VariantCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.entity.Variant;
import org.mapstruct.*;

import java.util.List;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface VariantMapper {
	Variant toEntity(VariantUpdateRequest req);

	Variant toEntity(VariantCreationRequest req);

	void toUpdate(VariantUpdateRequest req, @MappingTarget Variant variant);

	@Mapping(source = "variant.image.url", target = "imageUrl")
	VariantResponse toResponse(Variant variant);

	List<VariantResponse> toResponseList(List<Variant> variants);
}
