package com.sam.miniecommerceapi.cart.entity;

import com.sam.miniecommerceapi.product.entity.Variant;
import com.sam.miniecommerceapi.common.entity.BaseEntity;
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
@Table(name = "cart_items", uniqueConstraints = {@UniqueConstraint(columnNames = {"cart_id", "variant_id"})})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    Variant variant;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    public BigDecimal getSubTotal() {
        return this.variant.getPrice().multiply(BigDecimal.valueOf(this.quantity));
    }
}
