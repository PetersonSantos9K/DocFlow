package com.PetersonSantos9K.docflow_api.ingestion.client.github;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class GitHubClientConfig{

    @Bean
    RestClient gitHubRestClient(RestClient.Builder builder,
                                GitHubClientProperties properties) {

        Duration connectTimeout = properties.getConnectTimeout() != null ? properties.getConnectTimeout() : Duration.ofSeconds(5);
        Duration readTimeout = properties.getReadTimeout() != null ? properties.getReadTimeout() : Duration.ofSeconds(5);

        var httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();
        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);

        builder.baseUrl(properties.getBaseUrl())
                .requestFactory(requestFactory)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);


        if(properties.getToken() != null && !properties.getToken().isBlank()){
            builder.defaultHeader("Authorization", "Bearer " + properties.getToken());
        }
        return builder.build();
    }
}
