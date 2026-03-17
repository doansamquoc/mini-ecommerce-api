package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.entity.AttributeOption;

import java.util.List;
import java.util.Set;

public interface AttributeOptionService {
    Set<AttributeOption> getAttributeOptionsById(List<Long> ids);
}
