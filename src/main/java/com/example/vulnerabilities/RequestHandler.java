package com.example.vulnerabilities;

import com.example.vulnerabilities.model.ScanRequest;
import com.example.vulnerabilities.model.Package;
import com.example.vulnerabilities.model.SecurityVulnerability;
import com.example.vulnerabilities.parser.packegeExtractor.PackageExtractorFactory;
import com.example.vulnerabilities.service.GitHubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestHandler {

    /**
     * Handle user's request
     * @param request received request
     * @return vulnerable packages
     * @throws JsonProcessingException
     */
    public String handle(ScanRequest request) throws JsonProcessingException {
        List<Package> packages =
                PackageExtractorFactory.getPackageExtractor(request.getEcosystem())
                        .extractPackage(request.getFileContent());

        List<SecurityVulnerability> vulnerablePackages =
                new GitHubService().getVulnerablePackages(packages, request.getEcosystem());

        // todo convert the list to json and return
        return "complete";
    }

}
