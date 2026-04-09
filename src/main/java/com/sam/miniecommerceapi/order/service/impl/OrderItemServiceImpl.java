package com.sam.miniecommerceapi.order.service.impl;

import com.sam.miniecommerceapi.order.entity.OrderItem;
import com.sam.miniecommerceapi.order.repository.OrderItemRepository;
import com.sam.miniecommerceapi.order.service.OrderItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemServiceImpl implements OrderItemService {
	OrderItemRepository repository;

	@Override
	public OrderItem save(OrderItem orderItem) {
		return repository.save(orderItem);
	}

	@Override
	public List<OrderItem> saveAll(List<OrderItem> orderItems) {
		return repository.saveAll(orderItems);
	}

	@Override
	public void delete(OrderItem orderItem) {
		repository.delete(orderItem);
	}

	@Override
	public void deleteAll(List<OrderItem> orderItems) {
		repository.saveAll(orderItems);
	}
}
