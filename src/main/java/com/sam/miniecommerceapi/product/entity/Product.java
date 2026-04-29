package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.product.dto.ProductOptionDto;
import com.sam.miniecommerceapi.upload.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SequenceGenerator(name = "id_generator", sequenceName = "products_id_seq")
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
	@OrderBy("position ASC")
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	List<ProductOption> options = new ArrayList<>();

	public void addVariant(ProductVariant variant) {
		this.variants.add(variant);
		variant.setProduct(this);
	}

	public void updateInfo(String name, String description, Supplier<String> slugGenerator) {
		if (name != null && !name.equals(this.name)) {
			this.name = name;
			this.slug = slugGenerator.get();
		}
	}


}
