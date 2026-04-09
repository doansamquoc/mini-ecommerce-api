package com.sam.miniecommerceapi.common.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum PaymentMethod {
	COD, PAYPAL, STRIPE;

	// @JsonCreator
	// public static PaymentMethod from(String value) {
	// 	if (value == null) return null;
	// 	for (PaymentMethod method : values()) {
	// 		if (method.name().equalsIgnoreCase(value)) return method;
	// 	}
	// 	throw BusinessException.of(ErrorCode.INVALID_ENUM_VALUE);
	// }
}
