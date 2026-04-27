package com.sam.miniecommerceapi.order.service.impl;

import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.constant.OrderStatus;
import com.sam.miniecommerceapi.common.dto.response.PageResponse;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.exception.FieldViolation;
import com.sam.miniecommerceapi.order.dto.request.CancelOrderRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderItemRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderRequest;
import com.sam.miniecommerceapi.order.dto.response.OrderResponse;
import com.sam.miniecommerceapi.order.entity.Order;
import com.sam.miniecommerceapi.order.entity.OrderItem;
import com.sam.miniecommerceapi.order.mapper.OrderMapper;
import com.sam.miniecommerceapi.order.repository.OrderRepository;
import com.sam.miniecommerceapi.order.service.OrderItemService;
import com.sam.miniecommerceapi.order.service.OrderService;
import com.sam.miniecommerceapi.order.util.OrderStateMachine;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.product.service.ProductVariantService;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
	OrderMapper mapper;
	UserService userService;
	OrderRepository repository;
	OrderItemService itemService;
	ProductVariantService productVariantService;

	@Override
	@Transactional
	public OrderResponse createOrder(Long userId, OrderRequest request) {
		User user = userService.findById(userId);
		Order order = mapper.toEntity(request);
		order.setUser(user);
		order.setOrderItems(new ArrayList<>());

		for (OrderItemRequest i : request.items()) {
			// Receiver the record number has been updated
			int affected = productVariantService.deductStock(i.variantId(), i.quantity());
			// If the record not change
			if (affected == 0) {
				throw BusinessException.of(ErrorCode.PRODUCT_OUT_OF_STOCK, new FieldViolation("id", "product.stock", i.variantId()));
			}

			ProductVariant variant = productVariantService.findById(i.variantId());
			OrderItem orderItem = createOrderItem(order, variant, i.quantity());

			order.addToOrderItems(orderItem);
		}

		order.calcTotalPrice();
		return mapper.toResponse(repository.save(order));
	}

	@Override
	public OrderResponse cancelOrder(Long orderId, CancelOrderRequest request) {
		Order order = findById(orderId);
		if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.CANCELED, order.getPaymentMethod())) {
			throw BusinessException.of(ErrorCode.ORDER_CANNOT_BE_CANCELED);
		}
		order.cancel(request.getCancellationReason());
		return mapper.toResponse(repository.save(order));
	}

	@Override
	public OrderResponse confirmOrder(Long orderId) {
		Order order = findById(orderId);
		if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.CONFIRMED, order.getPaymentMethod())) {
			throw BusinessException.of(ErrorCode.ORDER_CANNOT_BE_CONFIRMED);
		}
		order.markAsConfirm();
		return mapper.toResponse(repository.save(order));
	}

	@Override
	public OrderResponse deliveringOrder(Long orderId) {
		Order order = findById(orderId);
		if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.DELIVERING, order.getPaymentMethod())) {
			throw BusinessException.of(ErrorCode.ORDER_CANNOT_BE_DELIVERING);
		}
		order.markAsDelivering();
		return mapper.toResponse(repository.save(order));
	}

	@Override
	public OrderResponse deliveredOrder(Long orderId) {
		Order order = findById(orderId);
		if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.DELIVERED, order.getPaymentMethod())) {
			throw BusinessException.of(ErrorCode.ORDER_CANNOT_BE_DELIVERED);
		}
		order.delivered();
		return mapper.toResponse(repository.save(order));
	}

	@Override
	public OrderResponse paymentPendingOrder(Long orderId) {
		Order order = findById(orderId);
		if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.PENDING_PAYMENT, order.getPaymentMethod())) {
			throw BusinessException.of(ErrorCode.ORDER_CANNOT_BE_PAYMENT_PENDING);
		}
		order.markAsPendingPayment();
		return mapper.toResponse(repository.save(order));
	}

	@Override
	public OrderResponse paymentPaidOrder(Long orderId) {
		Order order = findById(orderId);
		if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.PENDING_PAYMENT, order.getPaymentMethod())) {
			throw BusinessException.of(ErrorCode.ORDER_CANNOT_BE_PAID);
		}
		order.markAsPaid();
		return mapper.toResponse(repository.save(order));
	}

	@Override
	public OrderResponse paymentFailedOrder(Long orderId) {
		Order order = findById(orderId);
		if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.PENDING_PAYMENT, order.getPaymentMethod())) {
			throw BusinessException.of(ErrorCode.ORDER_CANNOT_BE_FAILED);
		}
		order.markAsFailed();
		return mapper.toResponse(repository.save(order));
	}

	@Override
	@Transactional
	public OrderResponse updateOrder(Long id, OrderRequest request) {
		Order order = findById(id);
		mapper.toEntity(request, order);

		Map<Long, OrderItem> existingItemsMap = order.getOrderItems().stream()
			.collect(Collectors.toMap(item -> item.getVariant().getId(), item -> item));

		for (OrderItemRequest iReq : request.items()) {
			// Order must be at least 1 product
			if (iReq.quantity() < 1) {
				throw BusinessException.of(ErrorCode.ORDER_INVALID_QUANTITY, new FieldViolation("id", "order.quantity.min", iReq.quantity()));
			}

			// Check current item already exists yet
			OrderItem existingItem = existingItemsMap.get(iReq.variantId());
			if (existingItem != null) {
				int quantityDiff = iReq.quantity() - existingItem.getQuantity();
				if (quantityDiff > 0) {
					int affected = productVariantService.deductStock(iReq.variantId(), iReq.quantity());
					if (affected == 0) {
						throw BusinessException.of(ErrorCode.PRODUCT_OUT_OF_STOCK, new FieldViolation("id", "product.stock.out_of_stock"));
					}
				} else if (quantityDiff < 0) {
					productVariantService.increaseStock(iReq.variantId(), Math.abs(quantityDiff));
				}

				existingItem.setQuantity(iReq.quantity());
				existingItemsMap.remove(iReq.variantId());
			} else {
				int affected = productVariantService.deductStock(iReq.variantId(), iReq.quantity());
				if (affected == 0) {
					throw BusinessException.of(ErrorCode.PRODUCT_OUT_OF_STOCK, new FieldViolation("id", "product.stock.out_of_stock"));
				}

				ProductVariant variant = productVariantService.findById(iReq.variantId());
				OrderItem newItem = createOrderItem(order, variant, iReq.quantity());

				order.addToOrderItems(newItem);
			}
		}

		List<OrderItem> removedItems = new ArrayList<>();
		for (OrderItem removedItem : existingItemsMap.values()) {
			productVariantService.increaseStock(removedItem.getVariant().getId(), removedItem.getQuantity());
			order.removeOrderItem(removedItem);
			removedItems.add(removedItem);
		}
		itemService.deleteAll(removedItems);

		order.calcTotalPrice();
		return mapper.toResponse(repository.save(order));
	}

	@Override
	public PageResponse<OrderResponse> getOrderByStatus(OrderStatus status, Pageable pageable) {
		Page<Order> orders = repository.findByStatus(status, pageable);
		Page<OrderResponse> responses = orders.map(mapper::toResponse);
		return PageResponse.from(responses);
	}

	@Override
	public PageResponse<OrderResponse> getOrderByUserId(Long userId, OrderStatus status, Pageable pageable) {
		Page<Order> orders = repository.findByUserIdAndStatus(userId, status, pageable);
		Page<OrderResponse> responses = orders.map(mapper::toResponse);
		return PageResponse.from(responses);
	}


	public Order findById(Long id) {
		return repository.findById(id).orElseThrow(() -> BusinessException.of(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
	}

	private OrderItem createOrderItem(Order order, ProductVariant variant, int quantity) {
		OrderItem orderItem = new OrderItem();
		orderItem.setVariant(variant);
		orderItem.setOrder(order);
		orderItem.setQuantity(quantity);
		orderItem.setUnitPrice(variant.getPrice());
		orderItem.calcTotalPrice();

		return orderItem;
	}
}
