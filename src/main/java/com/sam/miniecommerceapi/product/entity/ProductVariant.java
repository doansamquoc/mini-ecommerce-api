package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.upload.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_variants")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SequenceGenerator(name = "id_generator", sequenceName = "product_variants_id_seq")
public class ProductVariant extends BaseEntity implements Serializable {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	Product product;

	String title;

	@Column(name = "sku", nullable = false, unique = true)
	String sku;

	@Column(name = "price", nullable = false)
	BigDecimal price;

	@Column(name = "stock", nullable = false)
	Integer stock;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image_id")
	Image image;

	String option1;
	String option2;
	String option3;

	@Version
	Long version;
}
