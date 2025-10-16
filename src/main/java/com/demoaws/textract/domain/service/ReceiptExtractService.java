package com.demoaws.textract.domain.service;

import com.demoaws.textract.domain.dto.ReceiptDto;
import com.demoaws.textract.domain.entity.Receipt;
import org.springframework.web.multipart.MultipartFile;

public interface ReceiptExtractService {
    ReceiptDto saveReceipt(MultipartFile file);
}
