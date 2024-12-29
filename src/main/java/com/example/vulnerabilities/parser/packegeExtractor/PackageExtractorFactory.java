package com.example.vulnerabilities.parser.packegeExtractor;

import com.example.vulnerabilities.model.EcoSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackageExtractorFactory {

    @Autowired
    NpmExtractor npmExtractor;

    public PackageExtractor getPackageExtractor(EcoSystem ecoSystem) {
        // In the future it will be replaced with switch case by ecosystem
        return npmExtractor;
    }
}
