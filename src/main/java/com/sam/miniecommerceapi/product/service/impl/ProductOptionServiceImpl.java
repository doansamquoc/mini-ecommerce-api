package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.exception.FieldViolation;
import com.sam.miniecommerceapi.product.dto.ProductOptionDto;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.ProductOption;
import com.sam.miniecommerceapi.product.mapper.ProductOptionMapper;
import com.sam.miniecommerceapi.product.repository.ProductOptionRepository;
import com.sam.miniecommerceapi.product.service.ProductOptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductOptionServiceImpl implements ProductOptionService {
	ProductOptionMapper mapper;
	ProductOptionRepository repository;

	@Override
	public void batchUpdate(Product product, List<ProductOptionDto> dtos) {
		if (dtos == null || dtos.isEmpty()) {
			product.getOptions().clear();
			return;
		}

		List<FieldViolation> violations = invalidFields(dtos);
		if (!violations.isEmpty()) throw BusinessException.of(ErrorCode.VALIDATION_FAILED, violations);

		Map<Long, ProductOption> existing = product.getOptions().stream(
		).filter(option -> option.getId() != null
		).collect(Collectors.toMap(ProductOption::getId, o -> o));

		Set<Long> requestIds = dtos.stream().map(ProductOptionDto::id).collect(Collectors.toSet());
		product.getOptions().removeIf(option -> !requestIds.contains(option.getId()));

		for (ProductOptionDto dto : dtos) {
			if (dto.id() != null && existing.containsKey(dto.id())) {
				ProductOption option = existing.get(dto.id());
				mapper.toUpdate(dto, option);
			} else {
				ProductOption option = mapper.toEntity(dto);
				option.setProduct(product);
				product.getOptions().add(option);
			}
		}
	}

	public List<ProductOption> mappingOptions(List<ProductOptionDto> dtos) {
		List<FieldViolation> violations = invalidFields(dtos);
		if (!violations.isEmpty()) throw BusinessException.of(ErrorCode.VALIDATION_FAILED, violations);
		return dtos.stream().map(mapper::toEntity).toList();
	}

	private FieldViolation nameViolation(String name, int index) {
		return new FieldViolation(String.format("options[%d].name", index), "product.option.name_duplicate", name);
	}

	private FieldViolation positionViolation(int position, int index) {
		return new FieldViolation(String.format("options[%d].position", index), "product.option.position_duplicate", position);
	}

	private FieldViolation valueViolation(String value, int index) {
		return new FieldViolation(String.format("options[%d].values", index), "product.option.value_duplicate", value);
	}

	private List<FieldViolation> invalidFields(List<ProductOptionDto> dtos) {
		if (dtos == null || dtos.isEmpty()) return new ArrayList<>();

		List<FieldViolation> violations = new ArrayList<>();
		Set<String> seenNames = new HashSet<>();
		Set<Integer> seenPosition = new HashSet<>();

		for (int i = 0; i < dtos.size(); i++) {
			ProductOptionDto dto = dtos.get(i);

			if (dto.name() != null && !seenNames.add(dto.name())) violations.add(nameViolation(dto.name(), i));
			if (dto.position() != null && !seenPosition.add(dto.position())) {
				violations.add(positionViolation(dto.position(), i));
			}

			violations.addAll(invalidValues(dto.values()));
		}

		return violations;
	}

	private List<FieldViolation> invalidValues(List<String> values) {
		if (values == null || values.isEmpty()) return new ArrayList<>();

		Set<String> seenValues = new HashSet<>();
		List<FieldViolation> violations = new ArrayList<>();

		for (int i = 0; i < values.size(); i++) {
			String value = values.get(i);
			if (value != null && !seenValues.add(value.toLowerCase())) violations.add(valueViolation(value, i));
		}

		return violations;
	}
}
