package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.upload.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity implements Serializable {
	@Column(name = "name", nullable = false)
	String name;

	@Column(name = "description", columnDefinition = "text")
	String description;

	@Column(name = "slug", nullable = false, unique = true)
	String slug;

	@Column(name = "regular_price")
	BigDecimal regularPrice;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image_id")
	Image image;

	@Builder.Default
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	Set<ProductVariant> variants = new LinkedHashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	List<ProductOption> options = new ArrayList<>();
}
