package com.demoaws.textract.domain.service;

import com.demoaws.textract.domain.dto.RekognitionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageRecognitionService {

    RekognitionResponse recognizeImage(MultipartFile file) throws IOException;
}
