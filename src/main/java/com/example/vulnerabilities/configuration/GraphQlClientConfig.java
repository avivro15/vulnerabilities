package com.example.vulnerabilities.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for creating HttpGraphQlClient
 */
@Configuration
public class GraphQlClientConfig {

    @Bean
    public HttpGraphQlClient graphQlClient() {
        WebClient webClient = WebClient.builder()
                .defaultHeader("Authorization", "Bearer " +
                        System.getenv().get("GITHUB_ACCESS_TOKEN"))
                .baseUrl("https://api.github.com/graphql").build();

        return HttpGraphQlClient.builder(webClient).build();
    }
}
