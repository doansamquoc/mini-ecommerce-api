package com.sam.miniecommerceapi.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessException extends RuntimeException {
    ErrorCode errorCode;
    Map<String, Object> data;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = Map.of();
    }

    public BusinessException(ErrorCode errorCode, Map<String, Object> data) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = data;
    }
}
