package com.example.xmlcomparetool.dto;

import lombok.Data;

@Data
public class XmlDifference {
    private String xPath;
    private String expectedValue;
    private String actualValue;

    public XmlDifference(String xPath, String expectedValue, String actualValue) {
        this.xPath = xPath;
        this.expectedValue = expectedValue;
        this.actualValue = actualValue;
    }
}