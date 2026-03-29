package com.sam.miniecommerceapi.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // Server
    SERVER_INTERNAL(9999, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),

    // Auth 10xx
    AUTH_UNAUTHORIZED(1001, HttpStatus.UNAUTHORIZED, "Unauthorized access"),
    AUTH_ACCESS_DENIED(1002, HttpStatus.FORBIDDEN, "Access denied"),
    AUTH_INVALID_CREDENTIALS(1003, HttpStatus.BAD_REQUEST, "Wrong credentials"),
    AUTH_FAILED(1004, HttpStatus.UNAUTHORIZED, "Authentication failed"),

    // Token 11xx
    TOKEN_REVOKED(1101, HttpStatus.UNAUTHORIZED, "Token revoked"),
    TOKEN_EXPIRED(1102, HttpStatus.UNAUTHORIZED, "Token expired"),
    TOKEN_NOT_FOUND(1103, HttpStatus.NOT_FOUND, "Token not found"),
    TOKEN_ALREADY_USED(1104, HttpStatus.BAD_REQUEST, "Token already used"),
    TOKEN_INVALID(1105, HttpStatus.UNAUTHORIZED, "Invalid token"),

    // Account 12xx
    ACCOUNT_LOCKED(1201, HttpStatus.LOCKED, "Account locked"),
    ACCOUNT_DISABLED(1202, HttpStatus.BAD_REQUEST, "Account disabled"),
    ACCOUNT_EXPIRED(1203, HttpStatus.BAD_REQUEST, "Account expired"),

    // User 2xxx
    USER_EMAIL_ALREADY_EXISTS(2001, HttpStatus.CONFLICT, "Email already exists"),
    USER_NOT_FOUND(2002, HttpStatus.NOT_FOUND, "User not found"),

    // Product 30xx
    PRODUCT_NOT_FOUND(3001, HttpStatus.NOT_FOUND, "Product not found"),
    PRODUCT_NAME_CONFLICT(3002, HttpStatus.CONFLICT, "Product name conflict"),
    PRODUCT_SLUG_CONFLICT(3003, HttpStatus.CONFLICT, "Product slug conflict"),
    PRODUCT_SKU_ALREADY_EXISTS(3004, HttpStatus.CONFLICT, "Product sku already exists"),
    PRODUCT_SKU_CONFLICT(3005, HttpStatus.CONFLICT, "Product sku conflict"),
    PRODUCT_ATTRIBUTE_OPTION_NOT_FOUND(
            3010, HttpStatus.NOT_FOUND, "Product attribute option not found"),
    PRODUCT_NAME_CANNOT_BE_BLANK(3011, HttpStatus.BAD_REQUEST, "Product name cannot be lank"),
    PRODUCT_NAME_SIZE(
            3012, HttpStatus.BAD_REQUEST, "Product name must be at least 2 to 255 characters"),
    PRODUCT_SLUG_CANNOT_BLANK(3013, HttpStatus.BAD_REQUEST, "Product slug cannot be lank"),
    PRODUCT_SLUG_SIZE(
            3014, HttpStatus.BAD_REQUEST, "Product slug must be at least 2 to 255 characters"),
    PRODUCT_DESCRIPTION_SIZE(
            3015, HttpStatus.BAD_REQUEST, "Product description must be at least 2 characters"),
    PRODUCT_IMAGE_URL_CANNOT_BE_BLANK(
            3016, HttpStatus.BAD_REQUEST, "Product image url cannot be blank"),
    PRODUCT_IMAGE_URL_MUST_BE_URL(3017, HttpStatus.BAD_REQUEST, "Product image url must be an url"),
    PRODUCT_SKU_CANNOT_BE_BLANK(3018, HttpStatus.BAD_REQUEST, "Product sku cannot be blank"),
    PRODUCT_SKU_SIZE(
            3019, HttpStatus.BAD_REQUEST, "Product sku must be at least 2 to 16 characters"),
    PRODUCT_PRICE_CANNOT_BE_NULL(3020, HttpStatus.BAD_REQUEST, "Product price cannot be null"),
    PRODUCT_STOCK_QUANTITY_CANNOT_BE_NULL(
            3021, HttpStatus.BAD_REQUEST, "Product stock quantity cannot be null"),
    PRODUCT_VARIANT_NOT_FOUND(3022, HttpStatus.NOT_FOUND, "Product variant not found"),
    PRODUCT_VARIANT_NOT_ENOUGH(3023, HttpStatus.BAD_REQUEST, "Product variant is not enough"),
    PRODUCT_OUT_OF_STOCK(3024, HttpStatus.BAD_REQUEST, "Product is not enough or out of stock"),

    // Category 31xx
    CATEGORY_NAME_CANNOT_BE_BLANK(3101, HttpStatus.BAD_REQUEST, "Category name cannot be blank"),
    CATEGORY_NAME_SIZE(
            3102, HttpStatus.BAD_REQUEST, "Category names must be at least 2 to 64 characters"),

    CATEGORY_SLUG_CANNOT_BE_BLANK(3103, HttpStatus.BAD_REQUEST, "Category slug cannot be blank"),
    CATEGORY_SLUG_SIZE(
            3104, HttpStatus.BAD_REQUEST, "Category slug must be at least 2 to 64 characters"),

    CATEGORY_IMAGE_URL_CANNOT_BE_BLANK(
            3105, HttpStatus.BAD_REQUEST, "Category image url cannot be blank"),
    CATEGORY_IMAGE_URL_MUST_BE_URL(
            3106, HttpStatus.BAD_REQUEST, "Category image url must be an url"),

    CATEGORY_NAME_CONFLICT(3107, HttpStatus.CONFLICT, "Category name conflict"),
    CATEGORY_SLUG_CONFLICT(3108, HttpStatus.CONFLICT, "Category slug conflict"),

    CATEGORY_NOT_FOUND(3199, HttpStatus.NOT_FOUND, "Category not found"),

    // Order 32xx
    ORDER_NOT_FOUND(3200, HttpStatus.NOT_FOUND, "Order not found"),
    ORDER_CANNOT_UPDATE(3201, HttpStatus.BAD_REQUEST, "Order cannot update"),
    ORDER_INVALID_QUANTITY(3202, HttpStatus.BAD_REQUEST, "Order invalid quantity"),
    ORDER_EMPTY(3203, HttpStatus.BAD_REQUEST, "Order item cannot be empty"),
    ORDER_CANNOT_BE_CANCELED(3204, HttpStatus.NOT_ACCEPTABLE, "Order cannot be canceled"),
    ORDER_CANCELLATION_DEADLINE_PASSED(3205, HttpStatus.NOT_ACCEPTABLE, "Order cancellation deadline passed"),
    ORDER_CANNOT_BE_CONFIRMED(3206, HttpStatus.NOT_ACCEPTABLE, "Order cannot be confirmed"),
    ORDER_CANNOT_BE_DELIVERING(3207, HttpStatus.NOT_ACCEPTABLE, "Order cannot be delivering"),
    ORDER_CANNOT_BE_DELIVERED(3208, HttpStatus.NOT_ACCEPTABLE, "Order cannot be delivered"),
    ORDER_CANNOT_BE_PAYMENT_PENDING(3209, HttpStatus.NOT_ACCEPTABLE, "Order cannot be pending payment"),
    ORDER_CANNOT_BE_PAID(3210, HttpStatus.NOT_ACCEPTABLE, "Order cannot be payment paid"),
    ORDER_CANNOT_BE_FAILED(3211, HttpStatus.NOT_ACCEPTABLE, "Order cannot be payment failed"),

    // Order item
    ORDER_ITEM_NOT_FOUND(3300, HttpStatus.NOT_FOUND, "Order item not found"),

    // Request 4xxx
    REQUEST_INVALID(4001, HttpStatus.BAD_REQUEST, "Invalid request"),
    USER_PASSWORD_TOO_SHORT(4002, HttpStatus.BAD_REQUEST, "Password must be at least 6 characters"),
    RESET_TOKEN_CANNOT_BLANK(4003, HttpStatus.BAD_REQUEST, "Token cannot be blank"),
    RESET_NEW_PASSWORD_CANNOT_BLANK(4004, HttpStatus.BAD_REQUEST, "New password cannot be blank");

    int errorCode;
    HttpStatus httpStatus;
    String message;
}
