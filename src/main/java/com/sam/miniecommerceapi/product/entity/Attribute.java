package com.sam.miniecommerceapi.product.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Attribute for product. Example Color, Size, Ram, Storage,...
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attributes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attribute extends BaseEntity {
    @Column(name = "name", nullable = false)
    String name;
}
