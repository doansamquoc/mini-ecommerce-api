package com.sam.miniecommerceapi.cart.entity;

import com.sam.miniecommerceapi.shared.entity.BaseEntity;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "carts",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "product_variant_id"})}
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    ProductVariant variant;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    public BigDecimal getTotalPrice() {
        return this.variant.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
