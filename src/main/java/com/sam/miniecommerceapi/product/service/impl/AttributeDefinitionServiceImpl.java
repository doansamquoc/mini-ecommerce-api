package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.product.dto.request.AttributeDefinitionCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.AttributeDefinitionUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeDefinitionResponse;
import com.sam.miniecommerceapi.product.entity.AttributeDefinition;
import com.sam.miniecommerceapi.product.entity.Category;
import com.sam.miniecommerceapi.product.mapper.AttributeDefinitionMapper;
import com.sam.miniecommerceapi.product.repository.AttributeDefinitionRepository;
import com.sam.miniecommerceapi.product.service.AttributeDefinitionService;
import com.sam.miniecommerceapi.product.service.CategoryService;
import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeDefinitionServiceImpl implements AttributeDefinitionService {
    CategoryService categoryService;
    AttributeDefinitionMapper mapper;
    AttributeDefinitionRepository repository;

    @Override
    public AttributeDefinitionResponse createAttribute(AttributeDefinitionCreationRequest request) {
        if (existsByCategoryIdAndAttributeKey(request.categoryId(), request.attributeKey())) {
            throw BusinessException.of(ErrorCode.ATTRIBUTE_DEFINITION_EXISTS);
        }
        Category category = categoryService.getReference(request.categoryId());
        AttributeDefinition attributeDefinition = mapper.toEntity(request);
        attributeDefinition.setCategory(category);

        return mapper.toResponse(save(attributeDefinition));
    }

    @Override
    public AttributeDefinitionResponse getById(Long id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    public AttributeDefinitionResponse getByAttributeKey(String attributeKey) {
        return mapper.toResponse(findByAttributeKey(attributeKey));
    }

    @Override
    public List<AttributeDefinitionResponse> getAllByCategoryId(Long categoryId) {
        return repository.findAllByCategoryId(categoryId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<AttributeDefinitionResponse> getAllAttributes() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public AttributeDefinitionResponse updateAttribute(Long id, AttributeDefinitionUpdateRequest request) {
        AttributeDefinition attributeDefinition = findById(id);
        mapper.toUpdate(attributeDefinition, request);
        return mapper.toResponse(save(attributeDefinition));
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteByCategoryId(Long categoryId) {
        repository.deleteByCategoryId(categoryId);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void attributeNormalizer(Long categoryId, Map<String, Object> attributes) {
        List<String> allowedKeys = repository.findAttributeKeysByCategoryId(categoryId);
        Map<String, Object> normalized = new HashMap<>();
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String normalizedKey = entry.getKey().toLowerCase();
            if (!allowedKeys.contains(normalizedKey)) {
                throw BusinessException.of(ErrorCode.ATTRIBUTE_DEFINITION_NOT_ACCEPTABLE);
            }
            normalized.put(normalizedKey, entry.getValue());
        }

        // Side effect
        attributes.clear();
        attributes.putAll(normalized);
    }

    public AttributeDefinition findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> BusinessException.of(ErrorCode.ATTRIBUTE_DEFINITION_NOT_FOUND)
        );
    }

    public AttributeDefinition findByAttributeKey(String attributeKey) {
        return repository.findByAttributeKey(attributeKey).orElseThrow(
                () -> BusinessException.of(ErrorCode.ATTRIBUTE_DEFINITION_NOT_FOUND)
        );
    }

    public AttributeDefinition save(AttributeDefinition attributeDefinition) {
        return repository.save(attributeDefinition);
    }

    public boolean existsByCategoryIdAndAttributeKey(Long categoryId, String attributeKey) {
        return repository.existsByCategoryIdAndAttributeKey(categoryId, attributeKey);
    }
}
