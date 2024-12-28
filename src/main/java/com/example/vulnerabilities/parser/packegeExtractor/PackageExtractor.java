package com.example.vulnerabilities.parser.packegeExtractor;

import com.example.vulnerabilities.model.Package;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface PackageExtractor {

    List<Package> extractPackage(String fileContent) throws JsonProcessingException;

}
