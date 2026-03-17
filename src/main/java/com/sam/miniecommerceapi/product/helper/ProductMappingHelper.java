package com.sam.miniecommerceapi.product.helper;

import com.sam.miniecommerceapi.product.dto.response.ProductAttributeResponse;
import com.sam.miniecommerceapi.product.entity.AttributeOption;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductMappingHelper {

    @Named("groupAttributes")
    public List<ProductAttributeResponse> groupAttributes(Set<AttributeOption> options) {
        if (options == null || options.isEmpty()) return Collections.emptyList();

        return options.stream()
                .collect(Collectors.groupingBy(AttributeOption::getAttribute))
                .entrySet().stream()
                .map(entry -> ProductAttributeResponse.builder()
                        .id(entry.getKey().getId())
                        .name(entry.getKey().getName())
                        .options(entry.getValue().stream()
                                .map(AttributeOption::getValue)
                                .toList()
                        ).build()
                ).toList();
    }
}
