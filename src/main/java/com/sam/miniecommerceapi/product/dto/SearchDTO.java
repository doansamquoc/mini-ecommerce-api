package com.sam.miniecommerceapi.product.dto;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FieldProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;

import java.math.BigDecimal;

@ProjectionConstructor
public record SearchDTO(
	@IdProjection
	Long id,

	@FieldProjection
	String name,

	@FieldProjection
	String slug,

	@FieldProjection
	BigDecimal regularPrice,

	@FieldProjection(path = "image.url")
	String imageUrl,

	@FieldProjection(path = "category.name")
	String categoryName
) {}
