package com.sam.miniecommerceapi.upload.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image extends BaseEntity {
	@Column(name = "public_id", unique = true, nullable = false)
	String publicId;

	@Column(name = "url", nullable = false, columnDefinition = "text")
	String url;
}
