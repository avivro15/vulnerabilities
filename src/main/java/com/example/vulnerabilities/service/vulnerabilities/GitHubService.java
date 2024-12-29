package com.example.vulnerabilities.service.vulnerabilities;

import com.example.vulnerabilities.model.EcoSystem;
import com.example.vulnerabilities.model.Package;
import com.example.vulnerabilities.model.SecurityVulnerability;
import com.example.vulnerabilities.model.version.VersionConstraintHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GitHubService implements VulnerabilitiesService {

    private final int MAX_RESULTS = 100;
    private final String QUERY =
            """
            query($ecosystem: SecurityAdvisoryEcosystem!, $package: String!, $first: Int!) {
                   securityVulnerabilities(ecosystem: $ecosystem, package: $package, first: $first) {
                     nodes {
                       severity
                       package {
                         name
                       }
                       firstPatchedVersion {
                         identifier
                       }
                       vulnerableVersionRange
                     }
                   }
                 }
            """;

    @Autowired
    private HttpGraphQlClient graphQlClient;

    public GitHubService() {}

    private List<SecurityVulnerability> extractVulnerabilitiesFromResponse(ClientGraphQlResponse response, String version) {
        // Extract the "data" from the response (the map of the response body)
        Map<String, Object> data = response.getData();

        // Get the 'securityVulnerabilities' part of the data
        Map<String, Object> securityVulnerabilities = (Map<String, Object>) data.get("securityVulnerabilities");

        // Get the list of nodes (vulnerabilities)
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) securityVulnerabilities.get("nodes");

        List<SecurityVulnerability> result = new ArrayList<>();

        // go over each node and check version constraint
        for (Map<String, Object> node : nodes) {
            String vulnerableVersionRange = (String) node.get("vulnerableVersionRange");
            // if version in range - add it to the list
            if (VersionConstraintHelper.isVersionInRange(version, vulnerableVersionRange)) {
                result.add(convertToPackageVulnerability(node, version));
            }
        }

        return result;
    }

    private SecurityVulnerability convertToPackageVulnerability(Map<String, Object> node, String version) {
        String severity = (String) node.get("severity");
        String name = (String) ((Map<String, Object>) node.get("package")).get("name");
        // todo check null
        String firstPatchedVersion = (String) ((Map<String, Object>) node.get("firstPatchedVersion")).get("identifier");

        return new SecurityVulnerability(severity, name, version, firstPatchedVersion);
    }

    @Override
    public List<SecurityVulnerability> getVulnerablePackages(List<Package> packages, EcoSystem ecoSystem) {
        List<SecurityVulnerability> result = new ArrayList<>();

        for (Package p : packages) {
            // TODO do not block
            ClientGraphQlResponse response = graphQlClient
                    .document(QUERY)
                    .variable("ecosystem", ecoSystem)
                    .variable("package", p.name())
                    .variable("first", MAX_RESULTS)
                    .execute().block();

            if (response != null) {
                result.addAll(extractVulnerabilitiesFromResponse(response, p.version()));
            }
        }

        return result;
    }

}
