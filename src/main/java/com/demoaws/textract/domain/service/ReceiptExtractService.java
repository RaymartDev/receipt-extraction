package com.demoaws.textract.domain.service;

import com.demoaws.textract.domain.dto.ReceiptDto;
import org.springframework.web.multipart.MultipartFile;

public interface ReceiptExtractService {
    ReceiptDto saveReceipt(MultipartFile file);
    ReceiptDto getReceiptById(Long id);
}
