package com.sam.miniecommerceapi.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sam.miniecommerceapi.common.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BusinessException extends RuntimeException {
	ErrorCode errorCode;
	List<FieldViolation> fieldViolations;
	Object[] args;

	private BusinessException(ErrorCode errorCode, List<FieldViolation> fieldViolations, Object... args) {
		super(errorCode.getMessageKey());
		this.errorCode = errorCode;
		this.fieldViolations = fieldViolations;
		this.args = args;
	}

	public static BusinessException of(ErrorCode errorCode) {
		return new BusinessException(errorCode, Collections.emptyList());
	}

	public static BusinessException withErrors(ErrorCode errorCode, List<FieldViolation> fieldViolations) {
		return new BusinessException(errorCode, fieldViolations);
	}

	public static BusinessException withArgs(ErrorCode errorCode, Object... args) {
		return new BusinessException(errorCode, Collections.emptyList(), args);
	}

	public static BusinessException of(ErrorCode errorCode, List<FieldViolation> fieldViolations) {
		return new BusinessException(errorCode, fieldViolations);
	}

	public static BusinessException of(ErrorCode errorCode, FieldViolation fieldViolation) {
		return new BusinessException(errorCode, List.of(fieldViolation));
	}


	// public static BusinessException of(ErrorCode errorCode, String field, String message, Object... args) {
	// 	return new BusinessException(errorCode, List.of(new MyFieldError(field, message)), args);
	// }
}
