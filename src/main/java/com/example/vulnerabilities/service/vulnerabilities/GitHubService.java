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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class GitHubService implements VulnerabilitiesService {

    // Response constants
    private final String SECURITY_KEY = "securityVulnerabilities";
    private final String NODES_KEY = "nodes";
    private final String VUL_RANGE_KEY = "vulnerableVersionRange";
    private final String SEVERITY_KEY = "severity";
    private final String PACKAGE_KEY = "package";
    private final String NAME_KEY = "name";
    private final String FIRST_PATCH_KEY = "firstPatchedVersion";
    private final String IDENTIFIER_KEY = "identifier";

    // Request constants
    private final int MAX_RESULTS = 100;
    private final String ECO_SYSTEM_VAR = "ecosystem";
    private final String PACKAGE_VAR = "package";
    private final String FIRST_VAR = "first";
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

    /**
     * Extract vulnerabilities from response
     * take in consideration the version constraints and return only the vulnerabilities for that version
     * Only the last part of the function is casting the vulnerabilities to an entity -
     * no need to go over the vulnerabilities twice
     * Security vulnerability class represents only the necessary parameters
     * @param response the graphql response to extract vulnerabilities from
     * @param version current package version to compare
     * @return list of security vulnerabilities (empty list if no vulnerabilities found)
     */
    private List<SecurityVulnerability> extractVulnerabilitiesFromResponse(ClientGraphQlResponse response, String version) {
        // Extract the "data" from the response (the map of the response body)
        Map<String, Object> data = response.getData();
        List<SecurityVulnerability> result = new ArrayList<>();

        // Check if data is empty
        if (data == null) {
            return result;
        }

        // Check if security vulnerabilities map is a map
        if (!(data.get(SECURITY_KEY) instanceof Map)) {
            return result;
        }

        // Get the securityVulnerabilities
        Map<String, Object> securityVulnerabilities = (Map<String, Object>) data.get(SECURITY_KEY);

        // Check if nodes is available
        if (!(securityVulnerabilities.get(NODES_KEY) instanceof List)) {
            return result;
        }

        // Get the list of nodes (vulnerabilities)
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) securityVulnerabilities.get(NODES_KEY);

        // go over each node and check version constraint
        for (Map<String, Object> node : nodes) {
            String vulnerableVersionRange = (String) node.get(VUL_RANGE_KEY);
            // if version in range - add it to the list
            if (VersionConstraintHelper.isVersionInRange(version, vulnerableVersionRange)) {
                result.add(convertToPackageVulnerability(node, version));
            }
        }

        return result;
    }

    /**
     * Convert node to SecurityVulnerability
     * @param node node to parse
     * @param version version of vulnerability
     * @return security vulnerability object
     */
    private SecurityVulnerability convertToPackageVulnerability(Map<String, Object> node, String version) {
        String severity = (String) node.get(SEVERITY_KEY);
        String name = "";
        String fisrtPatch = "";

        if (node.get(PACKAGE_KEY) instanceof Map) {
            name = (String) ((Map<String, Object>) node.get(PACKAGE_KEY)).get(NAME_KEY);
        }

        if (node.get(FIRST_PATCH_KEY) instanceof Map) {
            fisrtPatch = (String) ((Map<String, Object>) node.get(FIRST_PATCH_KEY)).get(IDENTIFIER_KEY);
        }

        return new SecurityVulnerability(severity, name, version, fisrtPatch);
    }

    @Override
    public List<SecurityVulnerability> getVulnerablePackages(List<Package> packages, EcoSystem ecoSystem) {
        // Create a list of CompletableFutures one for each package
        List<CompletableFuture<List<SecurityVulnerability>>> futures = new ArrayList<>();

        // execute the graphql request on a separate thread for each package
        for (Package p : packages) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                ClientGraphQlResponse response = graphQlClient
                        .document(QUERY)
                        .variable(ECO_SYSTEM_VAR, ecoSystem)
                        .variable(PACKAGE_VAR, p.name())
                        .variable(FIRST_VAR, MAX_RESULTS)
                        .execute().block();

                if (response != null) {
                    return extractVulnerabilitiesFromResponse(response, p.version());
                } else {
                    return new ArrayList<>();
                }
            }));
        }

        // Wait for all tasks to complete and combine results
        return futures.stream()
                .map(CompletableFuture::join) // Wait for each future to complete
                .flatMap(List::stream)        // Flatten the lists of vulnerabilities
                .collect(Collectors.toList());
    }
}
