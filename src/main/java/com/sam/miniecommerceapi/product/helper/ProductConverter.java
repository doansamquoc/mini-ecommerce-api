package com.sam.miniecommerceapi.product.helper;

import com.sam.miniecommerceapi.product.dto.response.AttributeResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.entity.Attribute;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.mapper.ProductMapper;
import com.sam.miniecommerceapi.product.mapper.VariantMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductConverter {
    ProductMapper mapper;
    VariantMapper variantMapper;

    public ProductDetailsResponse toDetailedResponse(Product product) {
        ProductDetailsResponse response = mapper.toDetailsResponse(product);
        response.setAttributes(extractAttributes(product));

        return response;
    }

    private List<AttributeResponse> extractAttributes(Product product) {
        Map<Attribute, Set<String>> attributes = new HashMap<>();

        product.getVariants().forEach(variant -> {
            if (variant.getValues() != null) {
                variant.getValues().forEach(val -> {
                    Attribute attribute = val.getAttribute();
                    attributes.computeIfAbsent(attribute, k -> new TreeSet<>()).add(val.getValue());
                });
            }
        });

        return attributes.entrySet().stream().map(this::attributeResponse).toList();
    }

    private AttributeResponse attributeResponse(Map.Entry<Attribute, Set<String>> entry) {
        AttributeResponse response = new AttributeResponse();
        response.setId(entry.getKey().getId());
        response.setName(entry.getKey().getName());
        response.setValues(entry.getValue());

        return response;
    }
}
