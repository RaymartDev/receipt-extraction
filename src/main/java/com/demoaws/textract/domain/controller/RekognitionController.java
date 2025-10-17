package com.demoaws.textract.domain.controller;

import com.demoaws.textract.domain.dto.RekognitionResponse;
import com.demoaws.textract.domain.service.ImageRecognitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/rekognition")
@RequiredArgsConstructor
@Tag(name = "Rekognition", description = "Image Recognition API")
public class RekognitionController {

    private final ImageRecognitionService rekognitionService;

    @PostMapping(value = "/celebrity", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Recognize celebrity in image")
    public ResponseEntity<RekognitionResponse> recognizeImages(
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        RekognitionResponse response = rekognitionService.recognizeImage(file);
        return ResponseEntity.ok(response);
    }
}
