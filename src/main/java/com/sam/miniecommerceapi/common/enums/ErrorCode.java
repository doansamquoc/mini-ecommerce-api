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
    PRODUCT_ATTRIBUTE_OPTION_NOT_FOUND(3010, HttpStatus.NOT_FOUND, "Product attribute option not found"),
    PRODUCT_NAME_CANNOT_BE_BLANK(3011, HttpStatus.BAD_REQUEST, "Product name cannot be lank"),
    PRODUCT_NAME_SIZE(3012, HttpStatus.BAD_REQUEST, "Product name must be at least 2 to 255 characters"),
    PRODUCT_SLUG_CANNOT_BLANK(3013, HttpStatus.BAD_REQUEST, "Product slug cannot be lank"),
    PRODUCT_SLUG_SIZE(3014, HttpStatus.BAD_REQUEST, "Product slug must be at least 2 to 255 characters"),
    PRODUCT_DESCRIPTION_SIZE(3015, HttpStatus.BAD_REQUEST, "Product description must be at least 2 characters"),
    PRODUCT_IMAGE_URL_CANNOT_BE_BLANK(1016, HttpStatus.BAD_REQUEST, "Product image url cannot be blank"),
    PRODUCT_IMAGE_URL_MUST_BE_URL(1017, HttpStatus.BAD_REQUEST, "Product image url must be an url"),
    PRODUCT_SKU_CANNOT_BE_BLANK(1018, HttpStatus.BAD_REQUEST, "Product sku cannot be blank"),
    PRODUCT_SKU_SIZE(1019, HttpStatus.BAD_REQUEST, "Product sku must be at least 2 to 16 characters"),
    PRODUCT_PRICE_CANNOT_BE_NULL(1020, HttpStatus.BAD_REQUEST, "Product price cannot be null"),
    PRODUCT_STOCK_QUANTITY_CANNOT_BE_NULL(1021, HttpStatus.BAD_REQUEST, "Product stock quantity cannot be null"),


    // Category 31xx
    CATEGORY_NAME_CANNOT_BE_BLANK(3101, HttpStatus.BAD_REQUEST, "Category name cannot be blank"),
    CATEGORY_NAME_SIZE(3102, HttpStatus.BAD_REQUEST, "Category names must be at least 2 to 64 characters"),

    CATEGORY_SLUG_CANNOT_BE_BLANK(3103, HttpStatus.BAD_REQUEST, "Category slug cannot be blank"),
    CATEGORY_SLUG_SIZE(3104, HttpStatus.BAD_REQUEST, "Category slug must be at least 2 to 64 characters"),

    CATEGORY_IMAGE_URL_CANNOT_BE_BLANK(3105, HttpStatus.BAD_REQUEST, "Category image url cannot be blank"),
    CATEGORY_IMAGE_URL_MUST_BE_URL(3106, HttpStatus.BAD_REQUEST, "Category image url must be an url"),

    CATEGORY_NAME_CONFLICT(3107, HttpStatus.CONFLICT, "Category name conflict"),
    CATEGORY_SLUG_CONFLICT(3108, HttpStatus.CONFLICT, "Category slug conflict"),

    CATEGORY_NOT_FOUND(3199, HttpStatus.NOT_FOUND, "Category not found"),

    // Request 4xxx
    REQUEST_INVALID(4001, HttpStatus.BAD_REQUEST, "Invalid request"),
    USER_PASSWORD_TOO_SHORT(4002, HttpStatus.BAD_REQUEST, "Password must be at least 6 characters");

    int errorCode;
    HttpStatus httpStatus;
    String message;
}
