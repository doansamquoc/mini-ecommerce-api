package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import com.sam.miniecommerceapi.shared.exception.BusinessException;
import com.sam.miniecommerceapi.product.entity.AttributeOption;
import com.sam.miniecommerceapi.product.repository.AttributeOptionRepository;
import com.sam.miniecommerceapi.product.service.AttributeOptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeOptionServiceImpl implements AttributeOptionService {
    AttributeOptionRepository repository;

    /**
     * Get all attribute options by ID
     *
     * @param ids List attribute option ID
     * @return Set of Attribute option
     */
    @Override
    public Set<AttributeOption> getAttributeOptionsById(List<Long> ids) {
        List<AttributeOption> options = repository.findAllById(ids);

        if (options.size() != ids.size()) throw new BusinessException(ErrorCode.PRODUCT_ATTRIBUTE_OPTION_NOT_FOUND);

        return new HashSet<>(options);
    }
}
