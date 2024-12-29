package com.example.vulnerabilities.parser.packegeExtractor;

import com.example.vulnerabilities.model.Package;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface PackageExtractor {

    /**
     * Extract packages from base64 file content
     * @param fileContent file to parse
     * @return list of dependencies
     * @throws JsonProcessingException if file's json couldn't be parsed
     * @throws IllegalArgumentException if file's base64 couldn't be parsed
     */
    List<Package> extractPackage(String fileContent) throws JsonProcessingException, IllegalArgumentException;

}
