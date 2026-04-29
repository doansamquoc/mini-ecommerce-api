package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_options")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SequenceGenerator(name = "id_generator", sequenceName = "product_options_id_seq")
public class ProductOption extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	Product product;
	String name;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "values", columnDefinition = "jsonb")
	List<String> values;

	Integer position;
}
