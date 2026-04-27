package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.exception.FieldViolation;
import com.sam.miniecommerceapi.product.dto.ProductOptionDto;
import com.sam.miniecommerceapi.product.entity.ProductOption;
import com.sam.miniecommerceapi.product.mapper.ProductOptionMapper;
import com.sam.miniecommerceapi.product.repository.ProductOptionRepository;
import com.sam.miniecommerceapi.product.service.ProductOptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductOptionServiceImpl implements ProductOptionService {
	ProductOptionMapper mapper;
	ProductOptionRepository repository;

	@Override
	public List<ProductOption> mapOptions(List<ProductOptionDto> reqs) {
		if (reqs == null || reqs.isEmpty()) return Collections.emptyList();

		Set<String> seenNames = new HashSet<>();
		Set<Integer> seenPositions = new HashSet<>();

		for (int i = 0; i < reqs.size(); i++) {
			ProductOptionDto dto = reqs.get(i);

			// Validate option name
			if (dto.name() != null && !seenNames.add(dto.name().toLowerCase())) {
				throw BusinessException.of(
					ErrorCode.VALIDATION_FAILED,
					new FieldViolation(String.format("options[%d].name", i), "product.option.name_duplicate", dto.name())
				);
			}

			// Validate option position
			if (dto.position() != null && !seenPositions.add(dto.position())) {
				throw BusinessException.of(
					ErrorCode.VALIDATION_FAILED,
					new FieldViolation(
						String.format("options[%d].position", i),
						"product.option.position_duplicate",
						dto.position()
					)
				);
			}

			// Validate option values
			validateValues(dto.values(), i);
		}
		return reqs.stream().map(mapper::toEntity).toList();
	}

	private void validateValues(List<String> values, int optionIndex) {
		if (values == null || values.isEmpty()) return;

		Set<String> seenValues = new HashSet<>();
		for (int i = 0; i < values.size(); i++) {
			String value = values.get(i);
			if (value != null && !seenValues.add(value.toLowerCase())) {
				throw BusinessException.of(
					ErrorCode.VALIDATION_FAILED,
					new FieldViolation(
						String.format("options[%d].values[%d]", optionIndex, i),
						"product.option.value_duplicate",
						value
					)
				);
			}
		}
	}
}
