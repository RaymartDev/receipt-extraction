package com.demoaws.textract.domain.service.impl;

import com.demoaws.textract.domain.record.ExtractTextResponse;
import com.demoaws.textract.domain.service.TextractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class TextractServiceImpl implements TextractService {

    private final TextractClient textractClient;

    @Override
    public ExtractTextResponse extractTextFromImage(MultipartFile file) {
        try{
            byte[] imageBytes = file.getBytes();

            var request = DetectDocumentTextRequest.builder()
                    .document(Document.builder()
                            .bytes(SdkBytes.fromByteArray(imageBytes))
                            .build())
                    .build();

            var response = textractClient.detectDocumentText(request);

            List<String> lines = response.blocks().stream()
                    .filter(block -> block.blockType() == BlockType.LINE)
                    .map(Block::text)
                    .toList();

            return new ExtractTextResponse(lines);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        } catch (TextractException e) {
            throw new RuntimeException("Textract Failed: " + e.getMessage(), e);
        }
    }
}