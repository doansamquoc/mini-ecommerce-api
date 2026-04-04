package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.product.entity.AttributeValue;
import com.sam.miniecommerceapi.product.repository.AttributeValueRepository;
import com.sam.miniecommerceapi.product.service.AttributeValueService;
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
public class AttributeValueServiceImpl implements AttributeValueService {
    AttributeValueRepository repository;

    /**
     * Get all attribute options by ID
     *
     * @param ids List attribute option ID
     * @return Set of Attribute option
     */
    @Override
    public Set<AttributeValue> findAllById(Set<Long> ids) {
        List<AttributeValue> options = repository.findAllById(ids);
        if (options.size() != ids.size()) throw new BusinessException(ErrorCode.PRODUCT_ATTRIBUTE_OPTION_NOT_FOUND);
        return new LinkedHashSet<>(options);
    }
}
