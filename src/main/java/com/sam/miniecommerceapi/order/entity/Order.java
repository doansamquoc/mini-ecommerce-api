package com.sam.miniecommerceapi.order.entity;

import com.sam.miniecommerceapi.shared.entity.BaseEntity;
import com.sam.miniecommerceapi.shared.constant.OrderStatus;
import com.sam.miniecommerceapi.shared.constant.PaymentMethod;
import com.sam.miniecommerceapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
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
    Instant orderDate;

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

    @Column(name = "canceled_reason")
    String canceledReason;

    @Column(name = "canceled_at")
    Instant canceledAt;

    @Column(name = "delivered_at", updatable = false)
    Instant deliveredAt;

    public void cancel(String reason) {
        this.setStatus(OrderStatus.CANCELED);
        this.setCanceledReason(reason);
        this.setCanceledAt(Instant.now());
    }

    public void delivered() {
        this.setStatus(OrderStatus.DELIVERED);
        this.setDeliveredAt(Instant.now());
    }

    public void markAsConfirm() {
        this.setStatus(OrderStatus.CONFIRMED);
    }

    public void markAsPaid() {
        this.setStatus(OrderStatus.PAID);
    }

    public void markAsDelivering() {
        this.setStatus(OrderStatus.DELIVERING);
    }

    public void markAsFailed() {
        this.setStatus(OrderStatus.FAILED);
    }

    public void markAsPendingPayment() {
        this.setStatus(OrderStatus.PENDING_PAYMENT);
    }

    public void calcTotalPrice() {
        this.setTotalPrice(this.orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    public void addToOrderItems(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }

    public void removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
    }
}
