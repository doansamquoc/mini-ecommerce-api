package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.upload.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends BaseEntity implements Serializable {
	@FullTextField(analyzer = "name_analyzer", projectable = Projectable.YES)
	@Column(name = "name", nullable = false, unique = true)
	String name;

	@ManyToOne
	@JoinColumn(name = "image_id")
	Image image;

	@Column(name = "slug")
	String slug;

	@OneToMany(mappedBy = "category", orphanRemoval = true)
	List<Product> products;
}
