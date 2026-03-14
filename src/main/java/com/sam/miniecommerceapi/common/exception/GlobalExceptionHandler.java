package com.sam.miniecommerceapi.common.exception;

import com.sam.miniecommerceapi.common.dto.response.api.ErrorApi;
import com.sam.miniecommerceapi.common.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        ErrorCode errorCode = ErrorCode.SERVER_INTERNAL;
        String message = e.getMessage();
        if (message == null || message.isBlank()) message = errorCode.getMessage();
        return ApiFactory.error(errorCode, request.getServletPath(), message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorApi> handleNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause instanceof  BusinessException be) {
                return ApiFactory.error(be.getErrorCode(), request.getServletPath());
            }
            cause = cause.getCause();
        }
        return ApiFactory.error(ErrorCode.REQUEST_INVALID, request.getServletPath());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        FieldError fieldError = e.getFieldError();
        if (fieldError == null || fieldError.getDefaultMessage() == null) {
            throw new IllegalStateException("Validation error without message!");
        }
        ErrorCode errorCode = ErrorCode.valueOf(fieldError.getDefaultMessage());
        return ApiFactory.error(errorCode, request.getServletPath());
    }
}
