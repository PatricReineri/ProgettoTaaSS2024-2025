package com.service.galleryservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${services.eventmanagement.url}")
    private String eventManagementServiceUrl;

    @Bean
    public WebClient eventManagementWebClient(WebClient.Builder builder) {
        return builder.baseUrl(eventManagementServiceUrl).build();
    }

}
