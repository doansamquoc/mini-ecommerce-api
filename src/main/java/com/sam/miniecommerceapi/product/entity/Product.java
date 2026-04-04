package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

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
public class Product extends BaseEntity {
    @FullTextField(analyzer = "name_analyzer")
    @Column(name = "name", nullable = false)
    String name;

    @FullTextField(analyzer = "name_analyzer")
    @Column(name = "description")
    String description;

    @KeywordField
    @Column(name = "slug", nullable = false, unique = true)
    String slug;

    @GenericField
    @Column(name = "min_price")
    BigDecimal minPrice;

    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Column(name = "image_url")
    String imageUrl;

    @Builder.Default
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Variant> variants = new LinkedHashSet<>();

    public void addVariant(Variant variant) {
        if (variants == null) variants = new LinkedHashSet<>();
        variants.add(variant);
        variant.setProduct(this);
    }
}
