package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductAttributeResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductVariantResponse;
import com.sam.miniecommerceapi.product.entity.Attribute;
import com.sam.miniecommerceapi.product.entity.AttributeOption;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.product.helper.ProductMappingHelper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = "spring",
        uses = {ProductVariantMapper.class, ProductMappingHelper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {
    Product toProduct(ProductCreationRequest r);

    @Mapping(target = "variants", source = "variants")
    @Mapping(target = "attributes", expression = "java(helper.groupAttributes(options))")
    ProductDetailsResponse toResponse(
            Product product,
            List<ProductVariant> variants,
            Set<AttributeOption> options,
            @Context ProductMappingHelper helper
    );

    ProductVariantResponse toVariantResponse(ProductVariant variant);

    ProductAttributeResponse toAttributeResponse(Attribute attribute);

    ProductResponse toSummaryResponse(Product product);
}
