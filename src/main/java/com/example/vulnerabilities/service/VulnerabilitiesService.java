package com.example.vulnerabilities.service;

import com.example.vulnerabilities.model.EcoSystem;
import com.example.vulnerabilities.model.Package;
import com.example.vulnerabilities.model.SecurityVulnerability;

import java.util.List;

public interface VulnerabilitiesService {

    List<SecurityVulnerability> getVulnerablePackages(List<Package> packages,
                                                                    EcoSystem ecoSystem);

}
