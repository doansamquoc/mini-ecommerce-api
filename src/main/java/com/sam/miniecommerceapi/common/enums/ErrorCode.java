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
  PRODUCT_NOT_FOUND(2001, HttpStatus.NOT_FOUND, "Product not found");

  int errorCode;
  HttpStatus httpStatus;
  String message;
}
