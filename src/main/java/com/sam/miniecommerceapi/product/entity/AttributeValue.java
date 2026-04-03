package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attribute_values")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeValue extends BaseEntity {
    @Column(name = "value", nullable = false)
    String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", nullable = false)
    Attribute attribute;
}
