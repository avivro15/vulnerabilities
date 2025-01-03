package com.example.vulnerabilities.controller;

import com.example.vulnerabilities.service.request.RequestHandler;
import com.example.vulnerabilities.model.ScanRequest;
import com.example.vulnerabilities.model.SecurityVulnerability;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Listen to scan requests
 */
@RestController
public class VulnerabilitiesController {

    @Autowired
    RequestHandler requestHandler;

    /**
     * Listen to post requests on /api/v1/vulnerabilities/scan
     * @param request ecosystem and file in base64
     * @return vulnerable packages
     */
    @PostMapping("/api/v1/vulnerabilities/scan")
    public List<SecurityVulnerability> Scan(@RequestBody ScanRequest request) {
        try {
            return requestHandler.handle(request);
        }
        catch (JsonProcessingException | IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, "The received file is unprocessable");
        }
    }
}
