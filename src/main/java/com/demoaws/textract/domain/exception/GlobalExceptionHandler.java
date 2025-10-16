package com.demoaws.textract.domain.exception;

import com.demoaws.textract.domain.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    /**
     * Handle generic exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleGeneralException(Exception ex) {
        ApiResponseDto<Void> response = ApiResponseDto.error(
                "An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Handle specific cases like record not found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponseDto<Void> response = ApiResponseDto.error(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handle invalid arguments or validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiResponseDto<Map<String, String>> response = ApiResponseDto.error(
                "Validation failed",
                HttpStatus.BAD_REQUEST,
                errors
        );
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle file upload size issues
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        ApiResponseDto<Void> response = ApiResponseDto.error(
                "File size exceeds the maximum allowed limit.",
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.badRequest().body(response);
    }
}