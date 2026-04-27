package com.sam.miniecommerceapi.order.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "variant_id", nullable = false)
	ProductVariant variant;

	@Column(name = "quantity")
	Integer quantity;

	@Column(name = "unit_price")
	BigDecimal unitPrice;

	@Column(name = "total_price")
	BigDecimal totalPrice;

	public void calcTotalPrice() {
		this.setTotalPrice(this.unitPrice.multiply(BigDecimal.valueOf(this.quantity)));
	}
}
