package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.product.dto.response.AttributeResponse;
import com.sam.miniecommerceapi.product.entity.Attribute;
import com.sam.miniecommerceapi.product.entity.AttributeValue;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.Variant;
import com.sam.miniecommerceapi.product.service.AttributeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeServiceImpl implements AttributeService {
    @Override
    public List<AttributeResponse> groupAttributes(Product product) {
        if (product == null || product.getVariants() == null) return Collections.emptyList();

        Map<Attribute, Set<String>> attributeMap = new HashMap<>();
        for (Variant variant : product.getVariants()) {
            if (variant.getValues() == null) continue;
            for (AttributeValue option : variant.getValues()) {
                if (option == null || option.getAttribute() == null) {
                    log.debug("Skipping invalid option: {}", option);
                    continue;
                }

                attributeMap.computeIfAbsent(option.getAttribute(), k -> new HashSet<>()).add(option.getValue());
            }
        }

        return attributeMap.entrySet().stream().map(this::buildAttributeResponse).collect(Collectors.toList());
    }

    private AttributeResponse buildAttributeResponse(Map.Entry<Attribute, Set<String>> entry) {
        return AttributeResponse.builder()
            .id(entry.getKey().getId())
            .name(entry.getKey().getName())
            .values(entry.getValue())
            .build();
    }
}
