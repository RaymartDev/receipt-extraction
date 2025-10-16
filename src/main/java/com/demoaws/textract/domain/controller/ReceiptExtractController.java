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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/receipt")
@Tag(name = "AWS Textract Controller", description = "Operation managing Textract")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ReceiptExtractController {

    private final ReceiptExtractService receiptExtractService;

    @PostMapping(value = "/extract",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Extract text from an uploaded image or document")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Receipt extracted successfully"),
            @ApiResponse(responseCode = "400",description = "Invalid file or request"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error")
    })
    public ResponseEntity<ApiResponseDto<ReceiptDto>> extractText(
            @Parameter(description = "Input file to extract",required = true)
            @RequestPart("file") MultipartFile file){

        try {
            ReceiptDto receipt = receiptExtractService.saveReceipt(file);
            ApiResponseDto<ReceiptDto> response = ApiResponseDto.success(
                    "Receipt saved successfully",
                    receipt,
                    HttpStatus.CREATED
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            ApiResponseDto<ReceiptDto> errorResponse = ApiResponseDto.error(
                    "Failed to save receipt: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

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
}
