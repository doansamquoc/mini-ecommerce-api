package com.sam.miniecommerceapi.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BusinessException extends RuntimeException {
    ErrorCode errorCode;
    Object[] args;
    Map<String, List<String>> fieldErrors;

    public BusinessException(ErrorCode errorCode) {
        this(errorCode, null, Collections.emptyMap());
    }

    public BusinessException(ErrorCode errorCode, Object... args) {
        this(errorCode, args, Collections.emptyMap());
    }

    public BusinessException(ErrorCode errorCode, Map<String, List<String>> fieldErrors) {
        this(errorCode, null, fieldErrors);
    }

    public BusinessException(ErrorCode errorCode, Object[] args, Map<String, List<String>> fieldErrors) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.args = args;
        this.fieldErrors = fieldErrors;
    }
}
