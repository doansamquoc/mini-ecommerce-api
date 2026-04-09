package com.sam.miniecommerceapi.order.util;

import com.sam.miniecommerceapi.common.constant.OrderStatus;
import com.sam.miniecommerceapi.common.constant.PaymentMethod;

public class OrderStateMachine {
	public static boolean canTransition(OrderStatus from, OrderStatus to, PaymentMethod method) {
		if (method != PaymentMethod.COD) {
			return switch (from) {
				case PENDING -> to == OrderStatus.CANCELED || to == OrderStatus.PENDING_PAYMENT;
				case PENDING_PAYMENT -> to == OrderStatus.PAID || to == OrderStatus.FAILED;
				case FAILED -> to == OrderStatus.PENDING_PAYMENT || to == OrderStatus.CANCELED;
				case PAID -> to == OrderStatus.CONFIRMED;
				case CONFIRMED -> to == OrderStatus.DELIVERING;
				case DELIVERING -> to == OrderStatus.DELIVERED;
				default -> false;
			};
		} else {
			return switch (from) {
				case PENDING -> to == OrderStatus.CANCELED || to == OrderStatus.CONFIRMED;
				case CONFIRMED -> to == OrderStatus.DELIVERING;
				case DELIVERING -> to == OrderStatus.DELIVERED;
				default -> false;
			};
		}
	}
}
