package com.sam.miniecommerceapi.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
	int status;
	ErrorDetails error;

	private ErrorResponse(int status, ErrorDetails error) {
		this.status = status;
		this.error = error;
	}

	public static ErrorResponse of(int status, int code, String message) {
		return new ErrorResponse(status, new ErrorDetails(code, message, null));
	}

	public static ErrorResponse of(int status, int code, String message, List<FieldViolation> fieldErrors) {
		return new ErrorResponse(status, new ErrorDetails(code, message, fieldErrors));
	}
}
