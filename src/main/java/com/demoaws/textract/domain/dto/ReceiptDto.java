package com.demoaws.textract.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReceiptDto {
    private String companyName;
    private String branch;
    private String managerName;
    private String cashierNumber;
    private Double subTotal;
    private Double cash;
    private Double change;
    private List<ReceiptItemDto> items;
}


