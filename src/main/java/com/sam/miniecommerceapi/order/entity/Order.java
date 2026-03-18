package com.sam.miniecommerceapi.order.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.common.enums.OrderStatus;
import com.sam.miniecommerceapi.common.enums.PaymentMethod;
import com.sam.miniecommerceapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "total_price")
    BigDecimal totalPrice;

    @CreationTimestamp
    @Column(name = "order_date", nullable = false)
    LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    @Builder.Default
    OrderStatus status = OrderStatus.PENDING;

    @Column(name = "shipping_address", nullable = false)
    String shippingAddress;

    @Column(name = "phone_number", nullable = false)
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<OrderItem> orderItems = new ArrayList<>();
}
