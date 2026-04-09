package com.sam.miniecommerceapi.order.service;

import com.sam.miniecommerceapi.order.entity.OrderItem;

import java.util.List;

public interface OrderItemService {
	OrderItem save(OrderItem orderItem);

	List<OrderItem> saveAll(List<OrderItem> orderItems);

	void delete(OrderItem orderItem);

	void deleteAll(List<OrderItem> orderItems);
}
