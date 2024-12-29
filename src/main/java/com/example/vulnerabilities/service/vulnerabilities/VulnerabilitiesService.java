package com.example.vulnerabilities.service.vulnerabilities;

import com.example.vulnerabilities.model.EcoSystem;
import com.example.vulnerabilities.model.Package;
import com.example.vulnerabilities.model.SecurityVulnerability;

import java.util.List;

/**
 * Vulnerability service to generate vulnerable packages for packages list and specified ecosystem
 */
public interface VulnerabilitiesService {

    List<SecurityVulnerability> getVulnerablePackages(List<Package> packages,
                                                                    EcoSystem ecoSystem);

}
