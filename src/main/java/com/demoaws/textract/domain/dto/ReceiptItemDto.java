package com.demoaws.textract.domain.dto;

import lombok.Data;

@Data
public class ReceiptItemDto {
    private String productName;
    private Integer quantity;
    private Double price;
}