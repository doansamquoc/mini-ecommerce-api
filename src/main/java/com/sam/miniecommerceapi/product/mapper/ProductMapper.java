package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.SearchDTO;
import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.Variant;
import org.mapstruct.*;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(
	componentModel = "spring",
	uses = {VariantMapper.class, CategoryMapper.class},
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {
	Product toEntity(ProductCreationRequest request);

	void toUpdate(@MappingTarget Product product, ProductUpdateRequest request);

	@Mapping(source = "product.image.url", target = "imageUrl")
	@Mapping(source = "product.category.name", target = "categoryName")
	ProductResponse toResponse(Product product);

	SearchDTO toSearch(Product product);

	@Mapping(target = "attributes", source = "variants", qualifiedByName = "aggregateAttributesFromVariants")
	@Mapping(source = "product.image.url", target = "imageUrl")
	ProductDetailsResponse toDetailsResponse(Product product);

	@Named("aggregateAttributesFromVariants")
	default Map<String, Set<Object>> aggregateAttributesFromVariants(Set<Variant> variants) {
		if (variants == null || variants.isEmpty()) return Collections.emptyMap();
		return variants.stream()
			.map(Variant::getAttributes)
			.filter(Objects::nonNull)
			.flatMap(map -> map.entrySet().stream())
			.collect(Collectors.groupingBy(
					Map.Entry::getKey,
					Collectors.mapping(
						Map.Entry::getValue,
						Collectors.toCollection(() -> new TreeSet<>(attributeComparator()))
					)
				)
			);
	}

	default Comparator<Object> attributeComparator() {
		return (obj1, obj2) -> {
			if (obj1 == null || obj2 == null) return 0;
			if (obj1 instanceof Number n1 && obj2 instanceof Number n2) {
				return Double.compare(n1.doubleValue(), n2.doubleValue());
			}

			if (obj1 instanceof String s1 && obj2 instanceof String s2) {
				return s1.compareTo(s2);
			}

			return obj1.toString().compareTo(obj2.toString());
		};
	}
}
