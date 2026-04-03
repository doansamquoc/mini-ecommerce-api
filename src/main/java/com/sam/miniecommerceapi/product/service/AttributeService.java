package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.response.AttributeResponse;
import com.sam.miniecommerceapi.product.entity.Product;

import java.util.List;

public interface AttributeService {
    List<AttributeResponse> groupAttributes(Product product);
}
