package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.shared.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends BaseEntity {
    @FullTextField(analyzer = "name_analyzer")
    @Column(name = "name", nullable = false, unique = true)
    String name;

    @Column(name = "image_url")
    String imageUrl;

    @Column(name = "slug")
    String slug;

    @OneToMany(mappedBy = "category")
    List<Product> products;
}
