package com.sam.miniecommerceapi.common.exception;

import com.sam.miniecommerceapi.common.dto.response.api.ErrorApi;
import com.sam.miniecommerceapi.common.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ErrorApi> handleBusiness(BusinessException e, HttpServletRequest request) {
        if (e.getData().isEmpty())
            return ApiFactory.error(e.getErrorCode(), request.getRequestURI());
        return ApiFactory.error(e.getErrorCode(), request.getRequestURI(), e.getData());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ErrorApi> handleNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause instanceof BusinessException be) {
                return ApiFactory.error(be.getErrorCode(), request.getServletPath(), cause.getMessage());
            }
            cause = cause.getCause();
        }
        return ApiFactory.error(ErrorCode.REQUEST_INVALID, request.getServletPath());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorApi> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        FieldError fieldError = e.getFieldError();
        if (fieldError == null || fieldError.getDefaultMessage() == null) {
            throw new IllegalStateException("Validation error without message!");
        }
        ErrorCode errorCode = ErrorCode.valueOf(fieldError.getDefaultMessage());
        return ApiFactory.error(errorCode, request.getServletPath());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<ErrorApi> handleAccessDenied(AuthorizationDeniedException ade, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.AUTH_ACCESS_DENIED;
        log.warn("Access denied: {}", ade.getMessage());
        return ApiFactory.error(errorCode, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorApi> handleGeneric(Exception e, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.SERVER_INTERNAL;
        String message = e.getMessage();
        if (message == null || message.isBlank()) message = errorCode.getMessage();
        log.error("Exception type: {}", e.getClass());
        return ApiFactory.error(errorCode, request.getServletPath(), message);
    }
}
