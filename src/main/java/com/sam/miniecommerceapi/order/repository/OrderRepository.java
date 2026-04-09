package com.sam.miniecommerceapi.order.repository;

import com.sam.miniecommerceapi.common.constant.OrderStatus;
import com.sam.miniecommerceapi.order.entity.Order;
import com.sam.miniecommerceapi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	Page<Order> findByStatus(OrderStatus status, Pageable pageable);

	Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable);

	Long user(User user);
}
