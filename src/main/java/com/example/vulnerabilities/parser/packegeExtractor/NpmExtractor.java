package com.example.vulnerabilities.parser.packegeExtractor;

import com.example.vulnerabilities.model.Package;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class NpmExtractor implements PackageExtractor {

    private final String DEPENDENCIES_ROOT = "dependencies";

    /**
     * Extract package from base64 file received from user as package.json file
     * @param fileContent base64 package.json file
     * @return list of package under "dependencies"
     */
    @Override
    public List<Package> extractPackage(String fileContent) throws JsonProcessingException, IllegalArgumentException {
        List<com.example.vulnerabilities.model.Package> packageList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        // Get the first node of the parsed base64 file
        JsonNode rootNode = objectMapper.readTree(FileDecodeHelper.decodeBase64File(fileContent));

        // Find the dependencies path
        JsonNode dependenciesNode = rootNode.path(DEPENDENCIES_ROOT);

        Iterator<String> fieldNames = dependenciesNode.fieldNames();

        while (fieldNames.hasNext()) {
            String packageName = fieldNames.next();
            String packageVersion = dependenciesNode.path(packageName).asText();
            packageList.add(new Package(packageName, packageVersion));
        }

        return packageList;
    }
}
