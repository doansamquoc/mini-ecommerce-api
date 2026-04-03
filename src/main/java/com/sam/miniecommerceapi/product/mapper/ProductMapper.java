package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = {VariantMapper.class, AttributeOptionMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {
    @Mapping(target = "category.id", source = "categoryId")
    Product toProduct(ProductCreationRequest request);

    void updateProduct(ProductUpdateRequest request, @MappingTarget Product product);

    ProductResponse toResponse(Product product);

    @Mapping(target = "variants", source = "variants")
    @Mapping(target = "attributes", source = "variants")
    ProductDetailsResponse toDetailsResponse(Product product);
}
