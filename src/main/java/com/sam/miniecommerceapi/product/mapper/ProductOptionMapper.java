package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.ProductOptionDto;
import com.sam.miniecommerceapi.product.entity.ProductOption;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductOptionMapper {
	ProductOption toEntity(ProductOptionDto dto);

	ProductOptionDto toDto(ProductOption option);

	ProductOption toUpdate(ProductOptionDto dto, @MappingTarget ProductOption option);
}
