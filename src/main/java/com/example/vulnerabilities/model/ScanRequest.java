package com.example.vulnerabilities.model;

public class ScanRequest {

    final EcoSystem ecosystem;
    final String fileContent;

    public ScanRequest(EcoSystem ecosystem, String fileContent) {
        this.ecosystem = ecosystem;
        this.fileContent = fileContent;
    }

    public EcoSystem getEcosystem() {
        return ecosystem;
    }

    public String getFileContent() {
        return fileContent;
    }

}
