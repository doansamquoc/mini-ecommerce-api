package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.product.dto.request.AttributeTermCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.AttributeTermUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeTermResponse;
import com.sam.miniecommerceapi.product.entity.Attribute;
import com.sam.miniecommerceapi.product.entity.AttributeTerm;
import com.sam.miniecommerceapi.product.mapper.AttributeTermMapper;
import com.sam.miniecommerceapi.product.repository.AttributeTermRepository;
import com.sam.miniecommerceapi.product.service.AttributeService;
import com.sam.miniecommerceapi.product.service.AttributeTermService;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import com.sam.miniecommerceapi.shared.exception.BusinessException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeTermServiceImpl implements AttributeTermService {
    AttributeTermMapper mapper;
    AttributeTermRepository repository;
    AttributeService attributeService;

    @Override
    public AttributeTermResponse createTerm(Long attributeId, AttributeTermCreationRequest request) {
        String normalizedName = request.name().trim().toUpperCase();
        if (existsByName(normalizedName)) throw new BusinessException(ErrorCode.PRODUCT_ATTRIBUTE_TERM_NAME_EXISTS);
        Attribute attribute = attributeService.findById(attributeId);

        AttributeTerm attributeTerm = mapper.toEntity(request);
        attributeTerm.setAttribute(attribute);
        attributeTerm.setName(normalizedName);

        return mapper.toResponse(save(attributeTerm));
    }

    @Override
    public AttributeTermResponse updateTerm(Long attributeId, Long id, AttributeTermUpdateRequest request) {
        String normalizedName = request.name().trim().toUpperCase();
        if (existsByName(normalizedName)) throw new BusinessException(ErrorCode.PRODUCT_ATTRIBUTE_TERM_NAME_EXISTS);

        AttributeTerm attributeTerm = findByAttributeIdAndId(attributeId, id);
        mapper.toUpdate(attributeTerm, request);
        attributeTerm.setName(normalizedName);
        return mapper.toResponse(save(attributeTerm));
    }

    @Override
    public AttributeTermResponse getTerm(Long attributeId, Long id) {
        return mapper.toResponse(findByAttributeIdAndId(attributeId, id));
    }

    @Override
    public List<AttributeTermResponse> getTerms(Long attributeId) {
        return mapper.toResponseList(findAllByAttributeId(attributeId));
    }

    @Override
    public void deleteTerm(Long attributeId, Long id) {
        deleteByAttributeIdAndId(attributeId, id);
    }

    @Override
    public void deleteAllTermsByAttributeId(Long attributeId) {
        deleteAllByAttributeId(attributeId);
    }

    @Override
    public void deleteAllTerms() {
        deleteAll();
    }

    @Override
    public Set<AttributeTerm> findAllById(Set<Long> ids) {
        List<AttributeTerm> options = repository.findAllById(ids);
        if (options.size() != ids.size()) throw new BusinessException(ErrorCode.PRODUCT_ATTRIBUTE_VALUE_NOT_FOUND);
        return new LinkedHashSet<>(options);
    }

    private List<AttributeTerm> findAllByAttributeId(Long attributeId) {
        return repository.findAllByAttributeId(attributeId);
    }

    private AttributeTerm findByAttributeIdAndId(Long attributeId, Long id) {
        return repository.findByAttributeIdAndId(attributeId, id).orElseThrow(
                () -> new BusinessException(ErrorCode.PRODUCT_ATTRIBUTE_TERM_NOT_FOUND)
        );
    }

    private boolean existsByName(String name) {
        return repository.existsByNameIgnoreCase(name);
    }

    private AttributeTerm save(AttributeTerm attributeTerm) {
        return repository.save(attributeTerm);
    }

    private void deleteByAttributeIdAndId(Long attributeId, Long id) {
        repository.deleteByAttributeIdAndId(attributeId, id);
    }

    private void deleteAllByAttributeId(Long attributeId) {
        repository.deleteAllByAttributeId(attributeId);
    }

    private void deleteAll() {
        repository.deleteAll();
    }
}
