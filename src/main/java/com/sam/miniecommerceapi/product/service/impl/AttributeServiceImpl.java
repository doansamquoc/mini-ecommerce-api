package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.product.dto.request.AttributeCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.AttributeUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeResponse;
import com.sam.miniecommerceapi.product.entity.Attribute;
import com.sam.miniecommerceapi.product.mapper.AttributeMapper;
import com.sam.miniecommerceapi.product.repository.AttributeRepository;
import com.sam.miniecommerceapi.product.service.AttributeService;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import com.sam.miniecommerceapi.shared.exception.BusinessException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeServiceImpl implements AttributeService {
    AttributeMapper mapper;
    AttributeRepository repository;

    @Override
    public AttributeResponse createAttribute(AttributeCreationRequest request) {
        String normalizedName = request.name().trim().toUpperCase();
        if (existByName(normalizedName)) throw new BusinessException(ErrorCode.PRODUCT_ATTRIBUTE_EXISTS);

        Attribute attribute = mapper.toEntity(request);
        attribute.setName(normalizedName);

        return mapper.toResponse(save(attribute));
    }

    @Override
    public AttributeResponse updateAttribute(Long id, AttributeUpdateRequest request) {
        String normalizedName = request.name().trim().toUpperCase();
        if (existByName(normalizedName)) throw new BusinessException(ErrorCode.PRODUCT_ATTRIBUTE_EXISTS);

        Attribute attribute = findById(id);
        mapper.toUpdate(attribute, request);
        attribute.setName(normalizedName);

        return mapper.toResponse(save(attribute));
    }

    @Override
    public AttributeResponse getAttribute(Long id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    public List<AttributeResponse> getAllAttributes() {
        return mapper.toResponseList(findAll());
    }

    @Override
    public void deleteAttribute(Long id) {
        delete(id);
    }

    @Override
    public void deleteAllAttributes() {
        deleteAll();
    }

    private boolean existByName(String name) {
        return repository.existsByNameIgnoreCase(name);
    }

    private Attribute save(Attribute attribute) {
        return repository.save(attribute);
    }

    @Override
    public Attribute findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_ATTRIBUTE_NOT_FOUND));
    }

    private List<Attribute> findAll() {
        return repository.findAll();
    }

    private void delete(Long id) {
        repository.deleteById(id);
    }

    private void deleteAll() {
        repository.deleteAll();
    }
}
