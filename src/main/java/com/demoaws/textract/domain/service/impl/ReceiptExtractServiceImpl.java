package com.demoaws.textract.domain.service.impl;

import com.demoaws.textract.common.ReceiptNormalizer;
import com.demoaws.textract.domain.dto.ReceiptDto;
import com.demoaws.textract.domain.dto.ReceiptRawDto;
import com.demoaws.textract.domain.entity.Receipt;
import com.demoaws.textract.domain.entity.ReceiptItem;
import com.demoaws.textract.domain.repository.ReceiptRepository;
import com.demoaws.textract.domain.service.ReceiptExtractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ReceiptExtractServiceImpl implements ReceiptExtractService {

    private final TextractClient textractClient;
    private final ReceiptRepository receiptRepository;

    private ReceiptRawDto extractReceiptFromImage(MultipartFile file) {
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

            return ReceiptRawDto.builder().lines(lines).build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        } catch (TextractException e) {
            throw new RuntimeException("Textract Failed: " + e.getMessage(), e);
        }
    }

    @Override
    public ReceiptDto saveReceipt(MultipartFile file) {
        ReceiptRawDto raw = extractReceiptFromImage(file);
        ReceiptDto dto = ReceiptNormalizer.normalize(raw);
        Receipt receipt = new Receipt();
        receipt.setCompanyName(dto.getCompanyName());
        receipt.setBranch(dto.getBranch());
        receipt.setManagerName(dto.getManagerName());
        receipt.setCashierNumber(dto.getCashierNumber());
        receipt.setSubTotal(dto.getSubTotal());
        receipt.setCash(dto.getCash());
        receipt.setChangeAmount(dto.getChange());

        receipt.setItems(dto.getItems().stream().map(itemDto -> {
            ReceiptItem item = new ReceiptItem();
            item.setProductName(itemDto.getProductName());
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(itemDto.getPrice());
            item.setReceipt(receipt);
            return item;
        }).collect(Collectors.toList()));

        receiptRepository.save(receipt);
        return dto;
    }

    @Override
    public ReceiptDto getReceiptById(Long id) {
        Optional<Receipt> receipt = receiptRepository.findById(id);
        return receipt.map(ReceiptNormalizer::toDto).orElse(null);
    }

    @Override
    public List<ReceiptDto> getAllReceipts() {
        List<Receipt> receiptList = receiptRepository.findAll();
        return receiptList.stream()
                .map(ReceiptNormalizer::toDto)
                .toList();
    }
}
