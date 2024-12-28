package com.example.vulnerabilities.service;

import com.example.vulnerabilities.model.EcoSystem;
import com.example.vulnerabilities.model.Package;
import com.example.vulnerabilities.model.SecurityVulnerability;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

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

    private final HttpGraphQlClient graphQlClient;

    public GitHubService() {
        WebClient webClient = WebClient.builder()
                // Todo move to enviorment var
                .defaultHeader("Authorization", "Bearer " + "123")
                .baseUrl("https://api.github.com/graphql").build();

        graphQlClient = HttpGraphQlClient.builder(webClient).build();
    }

    private List<SecurityVulnerability> extractVulnerabilitiesFromResponse(ClientGraphQlResponse response, String version) {
        // Extract the "data" from the response (the map of the response body)
        Map<String, Object> data = (Map<String, Object>) response.toMap().get("data");

        // Get the 'securityVulnerabilities' part of the data
        Map<String, Object> securityVulnerabilities = (Map<String, Object>) data.get("securityVulnerabilities");

        // Get the list of nodes (vulnerabilities)
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) securityVulnerabilities.get("nodes");

        // Convert nodes into PackageVulnerability objects
        // insert only the relevant vulnerabilities based on version
        return nodes.stream()
                .map(this::convertToPackageVulnerability)
                .filter(vulnerability -> vulnerability.isVersionVulnerable(version))
                .collect(Collectors.toList());
    }

    private SecurityVulnerability convertToPackageVulnerability(Map<String, Object> node) {
        String severity = (String) node.get("severity");
        String name = (String) ((Map<String, Object>) node.get("package")).get("name");
        // todo check null
        String firstPatchedVersion = (String) ((Map<String, Object>) node.get("firstPatchedVersion")).get("identifier");
        String vulnerableVersionRange = (String) node.get("vulnerableVersionRange");

        return new SecurityVulnerability(severity, name, vulnerableVersionRange, firstPatchedVersion);
    }

    @Override
    public List<SecurityVulnerability> getVulnerablePackages(List<Package> packages, EcoSystem ecoSystem) {
        List<SecurityVulnerability> result = new ArrayList<>();

        for (Package p : packages) {
            // TODO do not block
            ClientGraphQlResponse response = graphQlClient
                    .document(QUERY)
                    .variable("ecosystem", ecoSystem)
                    .variable("package", p.getName())
                    .variable("first", MAX_RESULTS)
                    .execute().block();

            if (response != null) {
                result.addAll(extractVulnerabilitiesFromResponse(response, p.getVersion()));
            }
        }

        return result;
    }

}
