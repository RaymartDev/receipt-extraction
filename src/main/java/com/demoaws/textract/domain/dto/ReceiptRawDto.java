package com.demoaws.textract.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public class ReceiptRawDto {
    @Getter
    private List<String> lines;
}