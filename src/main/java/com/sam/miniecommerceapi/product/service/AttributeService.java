package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.request.AttributeCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.AttributeUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeResponse;
import com.sam.miniecommerceapi.product.entity.Attribute;
import com.sam.miniecommerceapi.product.entity.Product;

import java.util.List;

public interface AttributeService {
    AttributeResponse createAttribute(AttributeCreationRequest request);

    AttributeResponse updateAttribute(Long id, AttributeUpdateRequest request);

    AttributeResponse getAttribute(Long id);

    List<AttributeResponse> getAllAttributes();

    void deleteAttribute(Long id);

    void deleteAllAttributes();

    Attribute findById(Long id);
}
