package com.sam.miniecommerceapi.product.helper;

import org.springframework.stereotype.Component;

@Component
public class ProductMappingHelper {

//    @Named("groupAttributesFromVariants")
//    public List<AttributeResponse> groupAttributesFromVariants(List<Variant> variants) {
//        if (variants == null || variants.isEmpty()) return Collections.emptyList();
//        return variants.stream()
//                .flatMap(v -> v.getOptions().stream())
//                .collect(Collectors.groupingBy(AttributeOption::getAttribute))
//                .entrySet().stream()
//                .map(entry -> AttributeResponse.builder()
//                        .id(entry.getKey().getId())
//                        .name(entry.getKey().getName())
//                        .values(entry.getValue().stream()
//                                .map(AttributeOption::getValue)
//                                .distinct()
//                                .toList()
//                        ).build()
//                ).toList();
//    }
}
