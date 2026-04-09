package com.sam.miniecommerceapi.common.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OrderStatus {
	PENDING, PENDING_PAYMENT, PAID, FAILED, CONFIRMED, DELIVERING, DELIVERED, CANCELED, REFUNDING, REFUNDED,
}
