package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.upload.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import java.io.Serializable;
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
public class Product extends BaseEntity implements Serializable {
	@FullTextField(analyzer = "name_analyzer", projectable = Projectable.YES)
	@Column(name = "name", nullable = false)
	String name;

	@FullTextField(analyzer = "name_analyzer", projectable = Projectable.YES)
	@Column(name = "description")
	String description;

	@KeywordField(projectable = Projectable.YES)
	@Column(name = "slug", nullable = false, unique = true)
	String slug;

	@Column(name = "regular_price")
	@GenericField(projectable = Projectable.YES)
	BigDecimal regularPrice;

	@IndexedEmbedded
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	Category category;

	@ManyToOne
	@IndexedEmbedded
	@JoinColumn(name = "image_id")
	@IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
	Image image;

	@Builder.Default
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	Set<Variant> variants = new LinkedHashSet<>();
}
