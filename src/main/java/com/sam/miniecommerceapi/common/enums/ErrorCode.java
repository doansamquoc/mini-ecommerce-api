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
    UNAUTHORIZED_ERROR(1001, HttpStatus.UNAUTHORIZED, "Unauthorized error"),
    INTERNAL_ERROR(1002, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    ACCESS_DENIED(1003, HttpStatus.FORBIDDEN, "You don't have permission"),
    TOKEN_REVOKED(1004, HttpStatus.UNAUTHORIZED, "Token has been revoked"),
    TOKEN_EXPIRED(1005, HttpStatus.UNAUTHORIZED, "Token has been expired"),
    TOKEN_NOT_FOUND(1006, HttpStatus.NOT_FOUND, "Token not found"),
    PRODUCT_NOT_FOUND(2001, HttpStatus.NOT_FOUND, "Product not found"),
    INVALID_REQUEST(3001, HttpStatus.BAD_REQUEST, "Invalid request"),
    INVALID_CREDENTIALS(4001, HttpStatus.BAD_REQUEST, "Invalid credentials");

    int errorCode;
    HttpStatus httpStatus;
    String message;
}
