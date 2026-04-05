package com.sam.miniecommerceapi.shared.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
public record ApiResponse<T>(boolean success, String message, T data, LocalDateTime timestamp) {
    public static <T> ApiResponse<T> of() {
        return new ApiResponse<>(true, "Success", null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(true, "Success", data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> of(String message, T data) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now());
    }
}
