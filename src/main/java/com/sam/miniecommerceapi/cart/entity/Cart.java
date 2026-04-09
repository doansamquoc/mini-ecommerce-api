package com.sam.miniecommerceapi.cart.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	User user;

	@Builder.Default
	@OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	Set<CartItem> cartItems = new LinkedHashSet<>();

	public BigDecimal getTotalPrice() {
		return this.cartItems.stream()
			.map(item -> item.getVariant().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public int getTotalItems() {
		return this.cartItems.size();
	}
}
