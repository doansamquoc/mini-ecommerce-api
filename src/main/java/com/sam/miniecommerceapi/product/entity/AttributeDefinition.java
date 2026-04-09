package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.common.constant.DataType;
import com.sam.miniecommerceapi.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attribute_definitions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"category", "attribute_key"})
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeDefinition extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Column(name = "attribute_key", nullable = false)
    String attributeKey;
    String attributeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    DataType dataType;

    @Builder.Default
    boolean required = false;
}
