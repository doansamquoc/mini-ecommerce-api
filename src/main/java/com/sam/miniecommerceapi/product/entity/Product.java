package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.upload.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@Indexed
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {
	@FullTextField(analyzer = "name_analyzer")
	@Column(name = "name", nullable = false)
	String name;

	@FullTextField(analyzer = "name_analyzer")
	@Column(name = "description")
	String description;

	@KeywordField
	@Column(name = "slug", nullable = false, unique = true)
	String slug;

	@GenericField
	@Column(name = "price")
	BigDecimal price;

	@IndexedEmbedded
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	Category category;

	@ManyToOne
	@JoinColumn(name = "image_id")
	Image image;

	@Builder.Default
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	Set<Variant> variants = new LinkedHashSet<>();
}
