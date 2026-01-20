package com.sam.miniecommerceapi.common.api.factory;

import com.sam.miniecommerceapi.common.api.ErrorApi;
import com.sam.miniecommerceapi.common.api.SuccessApi;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ApiFactory {
  public static <T> SuccessApi<T> successBuild(int status, T data, String message) {
    return SuccessApi.<T>builder()
        .status(status)
        .data(data)
        .message(message)
        .timestamp(LocalDateTime.now())
        .build();
  }

  private static ErrorApi errorBuild(ErrorCode errorCode, String path, String customMessage) {
    return ErrorApi.builder()
        .errorCode(errorCode.getErrorCode())
        .error(errorCode.name())
        .status(errorCode.getHttpStatus().value())
        .message(customMessage != null ? customMessage : errorCode.getMessage())
        .path(path)
        .timestamp(LocalDateTime.now())
        .build();
  }

  public static <T> ResponseEntity<SuccessApi<T>> created(T data, String message) {
    SuccessApi<T> response = successBuild(HttpStatus.CREATED.value(), data, message);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  public static <T> ResponseEntity<SuccessApi<T>> success(String message) {
    SuccessApi<T> response = successBuild(HttpStatus.OK.value(), null, message);
    return ResponseEntity.ok(response);
  }

  public static <T> ResponseEntity<SuccessApi<T>> success(T data, String message) {
    SuccessApi<T> response = successBuild(HttpStatus.OK.value(), data, message);
    return ResponseEntity.ok(response);
  }

  public static <T> ResponseEntity<SuccessApi<T>> success(String cookie, T data, String message) {
    SuccessApi<T> response = successBuild(HttpStatus.OK.value(), data, message);
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body(response);
  }

  public static ResponseEntity<ErrorApi> error(ErrorCode errorCode, String path) {
    ErrorApi response = errorBuild(errorCode, path, null);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  public static ResponseEntity<ErrorApi> error(ErrorCode errorCode, String path, String message) {
    ErrorApi response = errorBuild(errorCode, path, message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }
}
