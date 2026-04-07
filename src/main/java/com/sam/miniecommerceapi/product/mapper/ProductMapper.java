package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeTermResponse;
import com.sam.miniecommerceapi.product.dto.response.AttributeWithTermListResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.AttributeTerm;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {VariantMapper.class, AttributeMapper.class, AttributeTermMapper.class, CategoryMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {
    Product toEntity(ProductCreationRequest request);

    void toUpdate(@MappingTarget Product product, ProductUpdateRequest request);

    ProductResponse toResponse(Product product);

    @Mapping(target = "attributes", source = "variants")
    ProductDetailsResponse toDetailsResponse(Product product);

    default Set<AttributeWithTermListResponse> mapVariantsToAttributes(Set<Variant> variants) {
        if (variants == null || variants.isEmpty()) return Collections.emptySet();

        return variants.stream()
                .flatMap(variant -> variant.getVariantAttributes().stream())
                .collect(Collectors.groupingBy(
                                AttributeTerm::getAttribute,
                                Collectors.mapping(
                                        term -> new AttributeTermResponse(term.getId(), term.getName()),
                                        Collectors.toSet()
                                )
                        )
                )
                .entrySet().stream()
                .map(entry -> new AttributeWithTermListResponse(
                        entry.getKey().getId(),
                        entry.getKey().getName(),
                        new ArrayList<>(entry.getValue())
                ))
                .collect(Collectors.toSet());
    }
}
