package com.sam.miniecommerceapi.order.dto.request;

import com.sam.miniecommerceapi.common.constant.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequest(
	@NotBlank(message = "order.shipping_address.required")
	@Size(min = 2, message = "order.shipping_address.min")
	String shippingAddress,

	@NotBlank(message = "order.phone_number.required")
	@Size(min = 9, max = 15, message = "order.phone_number.size")
	String phoneNumber,

	@NotNull(message = "order.payment_method.required")
	PaymentMethod paymentMethod,

	@Valid
	@NotEmpty(message = "order.items.required")
	List<OrderItemRequest> items
) {}
