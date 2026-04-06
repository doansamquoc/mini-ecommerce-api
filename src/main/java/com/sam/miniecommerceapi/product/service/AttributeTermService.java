package com.sam.miniecommerceapi.product.service;

import com.sam.miniecommerceapi.product.dto.request.AttributeTermCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.AttributeTermUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeTermResponse;
import com.sam.miniecommerceapi.product.entity.AttributeTerm;

import java.util.List;
import java.util.Set;

public interface AttributeTermService {
    AttributeTermResponse createTerm(Long attributeId, AttributeTermCreationRequest request);

    AttributeTermResponse updateTerm(Long attributeId, Long id, AttributeTermUpdateRequest request);

    AttributeTermResponse getTerm(Long attributeId, Long id);

    List<AttributeTermResponse> getTerms(Long attributeId);

    void deleteTerm(Long attributeId, Long id);

    void deleteAllTermsByAttributeId(Long attributeId);

    void deleteAllTerms();

    Set<AttributeTerm> findAllById(Set<Long> ids);
}
