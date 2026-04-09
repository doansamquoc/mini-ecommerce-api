package com.sam.miniecommerceapi.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sam.miniecommerceapi.common.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BusinessException extends RuntimeException {
	ErrorCode errorCode;
	List<MyFieldError> myFieldErrors;
	Object[] args;

	private BusinessException(ErrorCode errorCode, List<MyFieldError> myFieldErrors, Object... args) {
		super(errorCode.getMessageKey());
		this.errorCode = errorCode;
		this.myFieldErrors = myFieldErrors;
		this.args = args;
	}

	public static BusinessException of(ErrorCode errorCode) {
		return new BusinessException(errorCode, null);
	}

	public static BusinessException withErrors(ErrorCode errorCode, List<MyFieldError> myFieldErrors) {
		return new BusinessException(errorCode, myFieldErrors);
	}

	public static BusinessException withArgs(ErrorCode errorCode, Object... args) {
		return new BusinessException(errorCode, null, args);
	}

	public static BusinessException of(ErrorCode errorCode, List<MyFieldError> myFieldErrors, Object... args) {
		return new BusinessException(errorCode, myFieldErrors, args);
	}

	public static BusinessException of(ErrorCode errorCode, String field, String message, Object... args) {
		return new BusinessException(errorCode, List.of(new MyFieldError(field, message)), args);
	}
}
