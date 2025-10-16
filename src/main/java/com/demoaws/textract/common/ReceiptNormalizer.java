package com.demoaws.textract.common;

import com.demoaws.textract.domain.dto.ReceiptDto;
import com.demoaws.textract.domain.dto.ReceiptItemDto;
import com.demoaws.textract.domain.dto.ReceiptRawDto;
import com.demoaws.textract.domain.entity.Receipt;
import com.demoaws.textract.domain.entity.ReceiptItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiptNormalizer {

    public static ReceiptDto toDto(Receipt receipt) {
        if (receipt == null) return null;

        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setCompanyName(receipt.getCompanyName());
        receiptDto.setBranch(receipt.getBranch());
        receiptDto.setManagerName(receipt.getManagerName());
        receiptDto.setCashierNumber(receipt.getCashierNumber());
        receiptDto.setSubTotal(receipt.getSubTotal());
        receiptDto.setCash(receipt.getCash());
        receiptDto.setChange(receipt.getChangeAmount());
        receiptDto.setItems(toDtoItemList(receipt.getItems()));
        return receiptDto;
    }

    private static List<ReceiptItemDto> toDtoItemList(List<ReceiptItem> receiptItems) {
        if (receiptItems == null) return null;

        return receiptItems.stream()
                .map(ReceiptNormalizer::toDtoItem)
                .collect(Collectors.toList());
    }

    private static ReceiptItemDto toDtoItem(ReceiptItem receiptItem) {
        ReceiptItemDto receiptItemDto = new ReceiptItemDto();
        receiptItemDto.setPrice(receiptItem.getPrice());
        receiptItemDto.setQuantity(receiptItem.getQuantity());
        receiptItemDto.setProductName(receiptItem.getProductName());
        return receiptItemDto;
    }

    public static ReceiptDto normalize(ReceiptRawDto raw) {
        List<String> lines = raw.getLines();
        ReceiptDto dto = new ReceiptDto();
        List<ReceiptItemDto> items = new ArrayList<>();

        // === Extract top info ===
        dto.setCompanyName(lines.get(0).trim());
        dto.setBranch(lines.get(1).trim());

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim().toLowerCase();

            if (line.equals("cashier:") && i + 1 < lines.size()) {
                dto.setCashierNumber(lines.get(i + 1).trim());
            }

            if (line.equals("manager:") && i + 1 < lines.size()) {
                dto.setManagerName(lines.get(i + 1).trim());
            }

            // === Parse Subtotal, Cash, Change ===
            if (line.contains("sub total") && i + 1 < lines.size()) {
                dto.setSubTotal(parseMoney(lines.get(i + 1)));
            }
            if (line.equals("cash") && i + 1 < lines.size()) {
                dto.setCash(parseMoney(lines.get(i + 1)));
            }
            if (line.equals("change") && i + 1 < lines.size()) {
                dto.setChange(parseMoney(lines.get(i + 1)));
            }
        }

        // === Extract items between headers and totals ===
        int startIndex = findIndex(lines, "price") + 1;
        int endIndex = findIndex(lines, "sub total");

        for (int i = startIndex; i < endIndex; i += 3) {
            if (i + 2 >= lines.size()) break;

            String productName = lines.get(i).trim();
            Integer quantity = parseIntSafe(lines.get(i + 1));
            Double price = parseMoney(lines.get(i + 2));

            if (productName.isEmpty() || price == null) continue;

            ReceiptItemDto item = new ReceiptItemDto();
            item.setProductName(productName);
            item.setQuantity(quantity);
            item.setPrice(price);
            items.add(item);
        }

        dto.setItems(items);
        return dto;
    }

    private static int findIndex(List<String> lines, String keyword) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).toLowerCase().contains(keyword.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }

    private static Double parseMoney(String value) {
        try {
            return Double.parseDouble(value.replaceAll("[^\\d.]", ""));
        } catch (Exception e) {
            return null;
        }
    }

    private static Integer parseIntSafe(String value) {
        try {
            return Integer.parseInt(value.replaceAll("\\D", ""));
        } catch (Exception e) {
            return 1; // default 1 if missing
        }
    }
}