package com.demoaws.textract.domain.controller;

import com.demoaws.textract.domain.dto.ApiResponseDto;
import com.demoaws.textract.domain.dto.ReceiptDto;
import com.demoaws.textract.domain.exception.ResourceNotFoundException;
import com.demoaws.textract.domain.service.ReceiptExtractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/receipts")
@Tag(name = "Receipt List", description = "Operation listing receipts")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ReceiptListController {

    private final ReceiptExtractService receiptExtractService;

    @GetMapping("/{id}")
    @Operation(summary = "Get extracted receipt by ID",
            description = "Retrieve a previously extracted receipt using its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receipt retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Receipt not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID or request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ApiResponseDto<ReceiptDto>> getReceiptById(
            @Parameter(description = "ID of the receipt to retrieve", required = true)
            @PathVariable("id") Long id) {
        try {
            ReceiptDto receipt = receiptExtractService.getReceiptById(id);

            if (receipt == null) {
                throw new ResourceNotFoundException("Receipt not found with ID " + id);
            }

            ApiResponseDto<ReceiptDto> response = ApiResponseDto.success(
                    "Receipt retrieved successfully",
                    receipt,
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseDto<ReceiptDto> errorResponse = ApiResponseDto.error(
                    "Failed to retrieve receipt: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all extracted receipts",
            description = "Retrieve a list of all extracted receipts stored in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receipts retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No receipts found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ApiResponseDto<List<ReceiptDto>>> getAllReceipts() {
        try {
            List<ReceiptDto> receipts = receiptExtractService.getAllReceipts();

            if (receipts == null || receipts.isEmpty()) {
                ApiResponseDto<List<ReceiptDto>> noContentResponse = ApiResponseDto.success(
                        "No receipts found",
                        Collections.emptyList(),
                        HttpStatus.NO_CONTENT
                );
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(noContentResponse);
            }

            ApiResponseDto<List<ReceiptDto>> response = ApiResponseDto.success(
                    "Receipts retrieved successfully",
                    receipts,
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponseDto<List<ReceiptDto>> errorResponse = ApiResponseDto.error(
                    "Failed to retrieve receipts: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
