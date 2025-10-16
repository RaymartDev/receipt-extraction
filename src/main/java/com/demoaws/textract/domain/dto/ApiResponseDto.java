package com.demoaws.textract.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiResponseDto<T> {
    private int statusCode;
    private String message;
    private T data;

    public static <T> ApiResponseDto<T> success(String message, T data, HttpStatus status) {
        return new ApiResponseDto<>(status.value(), message, data);
    }

    public static <T> ApiResponseDto<T> error(String message, HttpStatus status) {
        return new ApiResponseDto<>(status.value(), message, null);
    }
}
