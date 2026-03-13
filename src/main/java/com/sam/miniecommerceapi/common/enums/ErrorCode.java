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
    EMAIL_ALREADY_EXISTED(2001, HttpStatus.CONFLICT, "Email already exists"),
    USER_NOT_FOUND(4002, HttpStatus.NOT_FOUND, "User not found"),

    // Product 3xxx
    PRODUCT_NOT_FOUND(3001, HttpStatus.NOT_FOUND, "Product not found"),

    // Request 4xxx
    REQUEST_INVALID(4001, HttpStatus.BAD_REQUEST, "Invalid request"),
    USER_PASSWORD_TOO_SHORT(3003, HttpStatus.BAD_REQUEST, "Password must be at least 6 characters");

    int errorCode;
    HttpStatus httpStatus;
    String message;
}
