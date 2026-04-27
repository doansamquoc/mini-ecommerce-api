package com.sam.miniecommerceapi.common.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
	// --- SERVER (50xx) ---
	SERVER_INTERNAL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "system.error.internal", 5000),
	INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST.value(), "system.error.invalid_body", 5001),
	VALIDATION_FAILED(HttpStatus.BAD_REQUEST.value(), "system.error.validation_failed", 5002),

	// --- AUTH (10xx) ---
	AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "auth.error.unauthorized", 1001),
	AUTH_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "auth.error.access_denied", 1002),
	AUTH_INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST.value(), "auth.error.invalid_credentials", 1003),
	AUTH_FAILED(HttpStatus.UNAUTHORIZED.value(), "auth.error.authentication_failed", 1004),

	// --- TOKEN (11xx) ---
	TOKEN_REVOKED(HttpStatus.UNAUTHORIZED.value(), "auth.token.revoked", 1101),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "auth.token.expired", 1102),
	TOKEN_INVALID(HttpStatus.UNAUTHORIZED.value(), "auth.token.invalid", 1103),

	// --- ACCOUNT (12xx) ---
	ACCOUNT_LOCKED(HttpStatus.LOCKED.value(), "account.error.locked", 1201),
	ACCOUNT_DISABLED(HttpStatus.BAD_REQUEST.value(), "account.error.disabled", 1202),
	ACCOUNT_EXPIRED(HttpStatus.BAD_REQUEST.value(), "account.error.expired", 1203),

	// --- USER (20xx) ---
	USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "user.error.email_exists", 2001),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "user.error.not_found", 2002),
	USER_PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST.value(), "user.validation.password_length", 2003),
	RESET_TOKEN_CANNOT_BLANK(HttpStatus.BAD_REQUEST.value(), "user.validation.token_required", 2004),
	RESET_NEW_PASSWORD_CANNOT_BLANK(HttpStatus.BAD_REQUEST.value(), "user.validation.password_required", 2005),

	// --- PRODUCT (30xx) ---
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "product.error.not_found", 3000),
	PRODUCT_SLUG_CONFLICT(HttpStatus.CONFLICT.value(), "product.error.slug_conflict", 3001),
	SKU_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "resource.error.conflict", 3002),
	SKU_CONFLICT(HttpStatus.CONFLICT.value(), "resource.error.conflict", 3002),
	PRODUCT_NAME_CANNOT_BE_BLANK(HttpStatus.BAD_REQUEST.value(), "product.validation.name_required", 3003),
	PRODUCT_NAME_SIZE(HttpStatus.BAD_REQUEST.value(), "product.validation.name_size", 3004),
	PRODUCT_SLUG_CANNOT_BLANK(HttpStatus.BAD_REQUEST.value(), "product.validation.slug_required", 3005),
	PRODUCT_SLUG_SIZE(HttpStatus.BAD_REQUEST.value(), "product.validation.slug_size", 3006),
	PRODUCT_DESCRIPTION_SIZE(HttpStatus.BAD_REQUEST.value(), "product.validation.description_size", 3007),
	PRODUCT_IMAGE_URL_CANNOT_BE_BLANK(HttpStatus.BAD_REQUEST.value(), "product.validation.image_required", 3008),
	PRODUCT_IMAGE_URL_MUST_BE_URL(HttpStatus.BAD_REQUEST.value(), "product.validation.image_invalid_url", 3009),
	PRODUCT_SKU_CANNOT_BE_BLANK(HttpStatus.BAD_REQUEST.value(), "product.validation.sku_required", 3010),
	PRODUCT_SKU_SIZE(HttpStatus.BAD_REQUEST.value(), "product.validation.sku_size", 3011),
	PRODUCT_PRICE_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST.value(), "product.validation.price_required", 3012),
	PRODUCT_STOCK_QUANTITY_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST.value(), "product.validation.stock_required", 3013),
	PRODUCT_VARIANT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "product.error.variant_not_found", 3014),
	PRODUCT_VARIANT_NOT_ENOUGH(HttpStatus.BAD_REQUEST.value(), "product.error.variant_not_enough", 3015),
	PRODUCT_OUT_OF_STOCK(HttpStatus.BAD_REQUEST.value(), "product.error.out_of_stock", 3016),
	PRODUCT_ATTRIBUTE_VALUE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "product.error.attribute_value_not_found", 3017),
	PRODUCT_ATTRIBUTE_EXISTS(HttpStatus.CONFLICT.value(), "product.error.attribute_name_exists", 3018),
	PRODUCT_ATTRIBUTE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "product.error.attribute_not_found", 3019),
	PRODUCT_ATTRIBUTE_TERM_NAME_EXISTS(HttpStatus.CONFLICT.value(), "product.error.attribute_term_name_exists", 3020),
	PRODUCT_ATTRIBUTE_TERM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "product.error.attribute_term_not_found", 3021),
	// --- CATEGORY (31xx) ---
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "category.error.not_found", 3100),
	CATEGORY_NAME_CANNOT_BE_BLANK(HttpStatus.BAD_REQUEST.value(), "category.validation.name_required", 3101),
	CATEGORY_NAME_SIZE(HttpStatus.BAD_REQUEST.value(), "category.validation.name_size", 3102),
	CATEGORY_SLUG_CANNOT_BE_BLANK(HttpStatus.BAD_REQUEST.value(), "category.validation.slug_required", 3103),
	CATEGORY_SLUG_SIZE(HttpStatus.BAD_REQUEST.value(), "category.validation.slug_size", 3104),
	CATEGORY_IMAGE_URL_CANNOT_BE_BLANK(HttpStatus.BAD_REQUEST.value(), "category.validation.image_required", 3105),
	CATEGORY_IMAGE_URL_MUST_BE_URL(HttpStatus.BAD_REQUEST.value(), "category.validation.image_invalid_url", 3106),
	CATEGORY_NAME_CONFLICT(HttpStatus.CONFLICT.value(), "category.error.name_conflict", 3107),
	CATEGORY_SLUG_CONFLICT(HttpStatus.CONFLICT.value(), "category.error.slug_conflict", 3108),

	// --- ORDER (32xx) ---
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "order.error.not_found", 3200),
	ORDER_CANNOT_UPDATE(HttpStatus.BAD_REQUEST.value(), "order.error.cannot_update", 3201),
	ORDER_INVALID_QUANTITY(HttpStatus.BAD_REQUEST.value(), "order.error.invalid_quantity", 3202),
	ORDER_EMPTY(HttpStatus.BAD_REQUEST.value(), "order.error.empty_items", 3203),
	ORDER_CANNOT_BE_CANCELED(HttpStatus.NOT_ACCEPTABLE.value(), "order.error.cannot_cancel", 3204),
	ORDER_CANCELLATION_DEADLINE_PASSED(HttpStatus.NOT_ACCEPTABLE.value(), "order.error.deadline_passed", 3205),
	ORDER_CANNOT_BE_CONFIRMED(HttpStatus.NOT_ACCEPTABLE.value(), "order.error.cannot_confirm", 3206),
	ORDER_CANNOT_BE_DELIVERING(HttpStatus.NOT_ACCEPTABLE.value(), "order.error.cannot_delivering", 3207),
	ORDER_CANNOT_BE_DELIVERED(HttpStatus.NOT_ACCEPTABLE.value(), "order.error.cannot_deliver", 3208),
	ORDER_CANNOT_BE_PAYMENT_PENDING(HttpStatus.NOT_ACCEPTABLE.value(), "order.error.cannot_be_pending", 3209),
	ORDER_CANNOT_BE_PAID(HttpStatus.NOT_ACCEPTABLE.value(), "order.error.cannot_be_paid", 3210),
	ORDER_CANNOT_BE_FAILED(HttpStatus.NOT_ACCEPTABLE.value(), "order.error.cannot_be_failed", 3211),
	ORDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "order.error.item_not_found", 3212),

	CART_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "cart.error.exists", 3301),
	CART_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "cart.error.not_found", 3302),


	ATTRIBUTE_DEFINITION_EXISTS(HttpStatus.CONFLICT.value(), "message.validation.conflict", 3401),
	ATTRIBUTE_DEFINITION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "message.validaton.not_found", 3402),
	ATTRIBUTE_DEFINITION_NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE.value(), "message.validation.not_acceptable", 3403),

	RESOURCE_CONFLICT(HttpStatus.CONFLICT.value(), "message.validation.conflict", 4901),
	DATABASE_CONSTRAINT_VIOLATION(HttpStatus.CONFLICT.value(), "message.error.conflict", 4902),

	INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST.value(), "enum.value.invalid", 4003),
	IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "image.not_found", 4101),
	// --- UPLOAD & REQUEST (4xxx) ---
	REQUEST_INVALID(HttpStatus.BAD_REQUEST.value(), "request.error.invalid", 4000),
	UPLOAD_FAILED(HttpStatus.BAD_REQUEST.value(), "upload.error.failed", 4001),
	UPLOAD_MAX_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST.value(), "upload.error.size_limit", 4002),
	CLOUDINARY_DELETE_FAILED(HttpStatus.BAD_REQUEST.value(), "cloudinary.delete.failed", 4004),
	PRODUCT_OPTION_NAME_DUPLICATE(HttpStatus.CONFLICT.value(), "product.options.name_duplicate", 300203);

	int status;
	String messageKey;
	int code;
}

