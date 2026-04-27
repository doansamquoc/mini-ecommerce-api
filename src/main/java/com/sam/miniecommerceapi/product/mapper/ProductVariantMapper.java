package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.VariantCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import org.mapstruct.*;

import java.util.List;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductVariantMapper {
	ProductVariant toEntity(VariantUpdateRequest req);

	ProductVariant toEntity(VariantCreationRequest req);

	void toUpdate(VariantUpdateRequest req, @MappingTarget ProductVariant variant);

	@Mapping(source = "image.url", target = "src")
	VariantResponse toResponse(ProductVariant variant);

	List<VariantResponse> toResponseList(List<ProductVariant> variants);
}
