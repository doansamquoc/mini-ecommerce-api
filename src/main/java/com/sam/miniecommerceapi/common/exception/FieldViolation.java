package com.sam.miniecommerceapi.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FieldViolation {
	String field;
	String message;
	Object[] args;

	public FieldViolation(String field, String message, Object...args) {
		this.field = field;
		this.message = message;
		this.args = args;
	}

	public FieldViolation(String field, String message) {
		this.field = field;
		this.message = message;
		this.args = new Object[0];
	}
}
