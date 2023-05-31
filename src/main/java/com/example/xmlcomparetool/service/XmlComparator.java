package com.example.xmlcomparetool.service;

import com.example.xmlcomparetool.dto.XmlDifference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class XmlComparator {
    
    private final ResourceLoader resourceLoader;

    public XmlComparator(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<XmlDifference> compareXmlFiles(String file1Path, String file2Path) throws IOException, SAXException, ParserConfigurationException {
        Resource file1Resource = resourceLoader.getResource("classpath:responses/" + file1Path);
        Resource file2Resource = resourceLoader.getResource("classpath:responses/" + file2Path);
        InputStream file1 = file1Resource.getInputStream();
        InputStream file2 = file2Resource.getInputStream();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc1 = builder.parse(file1);
        Document doc2 = builder.parse(file2);

        Diff xmlDiff = DiffBuilder.compare(doc1).withTest(doc2).build();
        List<Difference> differences = (List<Difference>) xmlDiff.getDifferences();

        List<XmlDifference> xmlDifferences = new ArrayList<>();
        for (Difference difference : differences) {
            String xPath = difference.getComparison().getControlDetails().getXPath();
            String expectedValue = String.valueOf(difference.getComparison().getControlDetails().getValue());
            String actualValue = String.valueOf(difference.getComparison().getTestDetails().getValue());

            XmlDifference xmlDifference = new XmlDifference(xPath, expectedValue, actualValue);
            xmlDifferences.add(xmlDifference);
        }

        return xmlDifferences;
    }
}
