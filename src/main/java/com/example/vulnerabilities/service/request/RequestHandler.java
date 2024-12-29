package com.example.vulnerabilities.service.request;

import com.example.vulnerabilities.model.ScanRequest;
import com.example.vulnerabilities.model.Package;
import com.example.vulnerabilities.model.SecurityVulnerability;
import com.example.vulnerabilities.parser.packegeExtractor.PackageExtractorFactory;
import com.example.vulnerabilities.service.vulnerabilities.VulnerabilitiesServiceFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestHandler {

    @Autowired
    private VulnerabilitiesServiceFactory vulnerabilitiesServiceFactory;

    @Autowired
    private PackageExtractorFactory packageExtractorFactory;

    /**
     * Handle user's request
     * @param request received request
     * @return vulnerable packages
     * @throws JsonProcessingException
     */
    public List<SecurityVulnerability> handle(ScanRequest request) throws JsonProcessingException, IllegalArgumentException {
        List<Package> packages =
                packageExtractorFactory.getPackageExtractor(request.ecosystem())
                        .extractPackage(request.fileContent());

        return vulnerabilitiesServiceFactory.getService()
                .getVulnerablePackages(packages, request.ecosystem());
    }
}
