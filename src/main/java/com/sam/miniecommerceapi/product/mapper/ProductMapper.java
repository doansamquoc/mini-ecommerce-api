package com.sam.miniecommerceapi.product.mapper;

import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import org.mapstruct.*;

import java.util.*;

@Mapper(
	componentModel = "spring",
	uses = {ProductVariantMapper.class, CategoryMapper.class},
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {
	@Mapping(target = "category", ignore = true)
	@Mapping(target = "image", ignore = true)
	Product toEntity(ProductCreationRequest request);

	@Mapping(target = "variants", ignore = true)
	@Mapping(target = "options", ignore = true)
	void toUpdate(@MappingTarget Product product, ProductUpdateRequest request);

	@Mapping(source = "product.image.url", target = "src")
	@Mapping(source = "product.category.name", target = "categoryName")
	ProductResponse toResponse(Product product);

	@Mapping(source = "product.image.url", target = "src")
	ProductDetailsResponse toDetailsResponse(Product product);

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
