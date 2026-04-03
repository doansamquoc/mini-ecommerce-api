package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.response.AttributeResponse;
import com.sam.miniecommerceapi.product.dto.response.AttributeValueResponse;
import com.sam.miniecommerceapi.product.entity.Attribute;
import com.sam.miniecommerceapi.product.entity.AttributeValue;
import com.sam.miniecommerceapi.product.entity.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.*;

@Mapper(componentModel = "spring")
public interface AttributeOptionMapper {
    @Mapping(source = "attribute.name", target = "attributeName")
    @Mapping(source = "value", target = "value")
    AttributeValueResponse toResponse(AttributeValue attributeValue);

    default List<AttributeResponse> toResponse(Set<Variant> variants) {
        if (variants == null) return Collections.emptyList();

        Map<Attribute, Set<String>> agg = new HashMap<>();

        variants.forEach(v -> v.getValues().forEach(val -> {
            if (val.getAttribute() != null) {
                Attribute attribute = val.getAttribute();
                agg.computeIfAbsent(attribute, k -> new TreeSet<>()).add(val.getValue());
            }
        }));

        return agg.entrySet().stream()
                .map(e -> new AttributeResponse(e.getKey().getId(), e.getKey().getName(), e.getValue()))
                .toList();
    }
}
