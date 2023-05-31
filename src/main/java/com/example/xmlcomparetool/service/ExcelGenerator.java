package com.example.xmlcomparetool.service;

import com.example.xmlcomparetool.dto.XmlDifference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class ExcelGenerator {

    public static Workbook generateExcelFile(List<XmlDifference> differences) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("XML Differences");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("XPath");
        headerRow.createCell(1).setCellValue("Expected Value");
        headerRow.createCell(2).setCellValue("Actual Value");

        // Populate rows with differences
        int rowNum = 1;
        for (XmlDifference difference : differences) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(difference.getXPath());
            row.createCell(1).setCellValue(difference.getExpectedValue());
            row.createCell(2).setCellValue(difference.getActualValue());
        }

        // Auto-size columns
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }
}
