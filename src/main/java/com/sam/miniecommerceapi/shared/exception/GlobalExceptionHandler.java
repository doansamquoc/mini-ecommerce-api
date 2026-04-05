package com.sam.miniecommerceapi.shared.exception;

import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class GlobalExceptionHandler {
    MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ErrorResponse> handle(BusinessException e, HttpServletRequest servletRequest, Locale locale) {
        String i18nMessage = messageSource.getMessage(e.getErrorCode().getMessageKey(), e.getArgs(), locale);
        ErrorResponse response = ErrorResponse.of(
                e.getErrorCode(),
                i18nMessage,
                servletRequest.getRequestURI(),
                servletRequest.getMethod(),
                e.getFieldErrors()
        );
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ErrorResponse> handle(HttpMessageNotReadableException e, HttpServletRequest request, Locale locale) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;
        String i18nMessage = messageSource.getMessage(errorCode.getMessageKey(), null, locale);
        String detail = e.getMostSpecificCause().getMessage();
        ErrorResponse response = ErrorResponse.of(errorCode, i18nMessage, detail, request.getRequestURI(), request.getMethod());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e, HttpServletRequest request, Locale locale) {
        Map<String, List<String>> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(fe -> messageSource.getMessage(
                                fe.getDefaultMessage(),
                                fe.getArguments(),
                                locale
                        ), Collectors.toList()))
                );
        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        String i18nMessage = messageSource.getMessage(errorCode.getMessageKey(), null, locale);
        ErrorResponse response = ErrorResponse.of(
                errorCode,
                i18nMessage,
                request.getRequestURI(),
                request.getMethod(),
                fieldErrors
        );
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<ErrorResponse> handleAccessDenied(AuthorizationDeniedException ade,
                                                     HttpServletRequest request,
                                                     Locale locale) {
        ErrorCode errorCode = ErrorCode.AUTH_ACCESS_DENIED;
        String i18nMessage = messageSource.getMessage(errorCode.getMessageKey(), null, locale);
        log.warn("Access denied: {}", ade.getMessage());

        ErrorResponse response = ErrorResponse.of(
                errorCode,
                i18nMessage,
                request.getRequestURI(),
                request.getMethod()
        );
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    ResponseEntity<ErrorResponse> handle(MaxUploadSizeExceededException e,
                                         HttpServletRequest request,
                                         Locale locale) {
        ErrorCode errorCode = ErrorCode.UPLOAD_MAX_SIZE_EXCEEDED;
        String i18nMessage = messageSource.getMessage(errorCode.getMessageKey(), null, locale);
        String detail = e.getMessage();

        ErrorResponse response = ErrorResponse.of(
                errorCode,
                i18nMessage,
                detail,
                request.getRequestURI(),
                request.getMethod()
        );
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }


    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleGeneric(Exception e, HttpServletRequest request, Locale locale) {
        ErrorCode errorCode = ErrorCode.SERVER_INTERNAL;
        String i18nMessage = messageSource.getMessage(errorCode.getMessageKey(), null, locale);
        String detail = e.getMessage();
        if (detail == null || detail.isBlank()) {
            detail = errorCode.getMessageKey();
        }
        log.error("Unhandled exception: {}", e.getClass().getName(), e);
        ErrorResponse response = ErrorResponse.of(
                errorCode,
                i18nMessage,
                detail,
                request.getRequestURI(),
                request.getMethod()
        );
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }
}
