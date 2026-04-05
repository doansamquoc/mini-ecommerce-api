package com.sam.miniecommerceapi.shared.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        boolean success,
        int code,
        String message,
        String detail,
        String path,
        String method,
        Map<String, List<String>> fieldErrors,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(ErrorCode errorCode, String i18nMessage, String path) {
        return new ErrorResponse(false, errorCode.getCode(), i18nMessage, null, path, null, null, LocalDateTime.now());
    }

    public static ErrorResponse of(ErrorCode errorCode, String i18nMessage, String path, String method) {
        return new ErrorResponse(false, errorCode.getCode(), i18nMessage, null, path, method, null, LocalDateTime.now());
    }

    public static ErrorResponse of(ErrorCode errorCode, String i18nMessage, String detail, String path, String method) {
        return new ErrorResponse(false, errorCode.getCode(), i18nMessage, detail, path, method, null, LocalDateTime.now());
    }

    public static ErrorResponse of(
            ErrorCode errorCode,
            String i18nMessage,
            String path,
            String method,
            Map<String, List<String>> fieldErrors
    ) {
        return new ErrorResponse(
                false,
                errorCode.getCode(),
                i18nMessage,
                null,
                path,
                method,
                fieldErrors,
                LocalDateTime.now()
        );
    }

    public static ErrorResponse of(
            ErrorCode errorCode,
            String i18nMessage,
            String detail,
            String path,
            String method,
            Map<String, List<String>> fieldErrors
    ) {
        return new ErrorResponse(
                false,
                errorCode.getCode(),
                i18nMessage,
                detail,
                path,
                method,
                fieldErrors,
                LocalDateTime.now()
        );
    }
}
