package com.example.xmlcomparetool.controller;

import com.example.xmlcomparetool.dto.XmlDifference;
import com.example.xmlcomparetool.service.ExcelGenerator;
import com.example.xmlcomparetool.service.XmlComparator;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class XmlComparisonController {

    private final XmlComparator xmlComparator;

    @Autowired
    public XmlComparisonController(XmlComparator xmlComparator) {
        this.xmlComparator = xmlComparator;
    }

    @GetMapping("/compare-xml")
    public ResponseEntity<InputStreamResource> compareXmlFiles() throws Exception {
        try {
            List<XmlDifference> differences = xmlComparator.compareXmlFiles("file1.xml", "file2.xml");
            Workbook workbook = ExcelGenerator.generateExcelFile(differences);

            // Prepare the Excel file for download
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));

            // Set headers and return the Excel file
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=differences.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);
        } catch (IOException e) {
            // Handle exceptions and return appropriate response
            return ResponseEntity.badRequest().build();
        }
    }
}
