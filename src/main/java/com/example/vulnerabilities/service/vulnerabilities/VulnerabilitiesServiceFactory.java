package com.example.vulnerabilities.service.vulnerabilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VulnerabilitiesServiceFactory {

    @Autowired
    GitHubService gitHubService;

    /**
     * NOTE - in the future the service provider will be selected by the user
     * @return Vulnerabilities service
     */
    public VulnerabilitiesService getService() {
        return gitHubService;
    }
}
