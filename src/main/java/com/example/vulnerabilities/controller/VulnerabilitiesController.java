package com.example.vulnerabilities.controller;

import com.example.vulnerabilities.RequestHandler;
import com.example.vulnerabilities.model.ScanRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VulnerabilitiesController {

    @Autowired
    RequestHandler requestHandler;

    /**
     * Listen to post requests on /api/v1/vulnerabilities/scan
     * @param request ecosystem and file
     * @return vulnerable packages
     */
    @PostMapping("/api/v1/vulnerabilities/scan")
    public String Scan(@RequestBody ScanRequest request) {

        // TODO validate the input
        try {
            requestHandler.handle(request);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "file";
    }

}
