package com.sam.miniecommerceapi.common.exception;

import com.sam.miniecommerceapi.common.api.ErrorApi;
import com.sam.miniecommerceapi.common.api.factory.ApiFactory;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorApi> handleBusiness(BusinessException e, HttpServletRequest request) {
    ErrorCode errorCode = e.getErrorCode();
    return ApiFactory.error(errorCode, request.getServletPath());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorApi> handleGeneric(Exception e, HttpServletRequest request) {
    ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
    String message = e.getMessage();
    if (message == null || message.isBlank()) message = errorCode.getMessage();
    return ApiFactory.error(errorCode, request.getServletPath(), message);
  }
}
