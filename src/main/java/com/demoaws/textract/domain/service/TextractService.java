package com.demoaws.textract.domain.service;

import com.demoaws.textract.domain.record.ExtractTextResponse;
import org.springframework.web.multipart.MultipartFile;

public interface TextractService {
    ExtractTextResponse extractTextFromImage(MultipartFile file);
}
