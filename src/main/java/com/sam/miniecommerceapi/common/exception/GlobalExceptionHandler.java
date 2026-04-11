package com.sam.miniecommerceapi.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.service.TranslatorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class GlobalExceptionHandler {
	MessageSource messageSource;
	TranslatorService translator;

	@ExceptionHandler(BusinessException.class)
	ResponseEntity<ErrorResponse> handle(BusinessException e, Locale locale) {
		log.error("Business Error: {}", e.getMessage());

		ErrorCode ec = e.getErrorCode();
		List<MyFieldError> fieldErrors = e.getMyFieldErrors().stream().filter(Objects::nonNull).map(f -> {
			String messaged = translator.translator(f.getMessage(), e.getArgs(), locale);
			return new MyFieldError(f.getField(), messaged);
		}).toList();

		String messaged = translator.translator(ec.getMessageKey(), null, locale);
		ErrorResponse response = ErrorResponse.of(ec.getStatus(), ec.getCode(), messaged, fieldErrors);
		return ResponseEntity.status(ec.getStatus()).body(response);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<ErrorResponse> handle(HttpMessageNotReadableException e, Locale locale) {
		log.error("Http Method Not Readable", e);

		List<MyFieldError> fieldErrors = new ArrayList<>();
		if (e.getCause() instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
			String fieldName = ife.getPath().getFirst().getFieldName();
			String messaged = translator.translator("enum.value.invalid", null, locale);
			fieldErrors.add(new MyFieldError(fieldName, messaged));
		}

		ErrorCode ec = ErrorCode.INVALID_REQUEST_BODY;
		String messaged = translator.translator(ec.getMessageKey(), null, locale);
		ErrorResponse response = ErrorResponse.of(ec.getStatus(), ec.getCode(), messaged, fieldErrors);

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e, Locale locale) {
		log.error("Validation Error", e);

		List<MyFieldError> fieldErrors = e.getBindingResult().getFieldErrors().stream().map(f -> {
			String messaged = translator.translator(f.getDefaultMessage(), f.getArguments(), locale);
			return new MyFieldError(f.getField(), messaged);
		}).toList();

		ErrorCode ec = ErrorCode.VALIDATION_FAILED;
		String messaged = translator.translator(ec.getMessageKey(), null, locale);
		ErrorResponse response = ErrorResponse.of(ec.getStatus(), ec.getCode(), messaged, fieldErrors);
		return ResponseEntity.status(ec.getStatus()).body(response);
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	ResponseEntity<ErrorResponse> handle(AuthorizationDeniedException e, Locale locale) {
		log.warn("Access Denied", e);

		ErrorCode ec = ErrorCode.AUTH_ACCESS_DENIED;
		String message = messageSource.getMessage(ec.getMessageKey(), null, locale);
		ErrorResponse response = ErrorResponse.of(ec.getStatus(), ec.getCode(), message);
		return ResponseEntity.status(ec.getStatus()).body(response);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	ResponseEntity<ErrorResponse> handle(MaxUploadSizeExceededException e, Locale locale) {
		log.warn("Max Upload Size", e);

		ErrorCode ec = ErrorCode.UPLOAD_MAX_SIZE_EXCEEDED;
		String messaged = translator.translator(ec.getMessageKey(), null, locale);
		ErrorResponse response = ErrorResponse.of(ec.getStatus(), ec.getCode(), messaged);
		return ResponseEntity.status(ec.getStatus()).body(response);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	ResponseEntity<ErrorResponse> handle(DataIntegrityViolationException e, Locale locale) {
		log.error("Database Constraint Violation", e);

		ErrorCode ec = ErrorCode.DATABASE_CONSTRAINT_VIOLATION;
		String messaged = translator.translator(ec.getMessageKey(), null, locale);
		ErrorResponse response = ErrorResponse.of(ec.getStatus(), ec.getCode(), messaged);
		return ResponseEntity.status(ec.getStatus()).body(response);
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorResponse> handleGeneric(Exception e, Locale locale) {
		log.error("Unhandled Exception: {}", e.getClass().getName(), e);

		ErrorCode ec = ErrorCode.SERVER_INTERNAL;
		String messaged = translator.translator(ec.getMessageKey(), null, locale);
		ErrorResponse response = ErrorResponse.of(ec.getStatus(), ec.getCode(), messaged);

		return ResponseEntity.status(ec.getStatus()).body(response);
	}
}
