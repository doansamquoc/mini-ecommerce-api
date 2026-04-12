package com.sam.miniecommerceapi.upload.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import java.io.Serializable;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image extends BaseEntity implements Serializable {
	@Column(name = "public_id", unique = true, nullable = false)
	String publicId;

	@Column(name = "url", nullable = false, columnDefinition = "text")
	@GenericField(projectable = Projectable.YES)
	String url;
}
