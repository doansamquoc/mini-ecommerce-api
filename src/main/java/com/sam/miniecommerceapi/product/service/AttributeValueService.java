package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.entity.AttributeValue;

import java.util.Set;

public interface AttributeValueService {
    Set<AttributeValue> findAllById(Set<Long> ids);
}
