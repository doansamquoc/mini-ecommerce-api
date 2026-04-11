package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.upload.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "variants")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Variant extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	Product product;

	@Column(name = "sku", nullable = false, unique = true)
	String sku;

	@Column(name = "price", nullable = false)
	BigDecimal price;

	@Column(name = "stock_quantity", nullable = false)
	Integer stockQuantity;

	@ManyToOne
	@JoinColumn(name = "image_id")
	Image image;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "attributes", columnDefinition = "jsonb")
	Map<String, Object> attributes;

	@Version
	Long version;
}
