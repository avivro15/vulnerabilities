package com.example.vulnerabilities.parser.packegeExtractor;

import com.example.vulnerabilities.model.EcoSystem;

public class PackageExtractorFactory {

    public static PackageExtractor getPackageExtractor(EcoSystem ecoSystem) {
        // In the future it will be replaced with switch case by ecosystem
        return new NpmExtractor();
    }

}
