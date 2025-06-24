package com.service.eventsetupservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.eventmanagement.url}")
    private String eventManagementServiceUrl;

    @Value("${services.galleryservice.url}")
    private String galleryServiceUrl;

    @Value("${services.boardservice.url}")
    private String boardServiceUrl;

    @Value("${services.guestgameservice.url}")
    private String guestGameServiceUrl;

    @Bean
    public WebClient eventManagementWebClient(WebClient.Builder builder) {
        return builder.baseUrl(eventManagementServiceUrl).build();
    }

    @Bean
    public WebClient galleryServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl(galleryServiceUrl).build();
    }

    @Bean
    public WebClient boardServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl(boardServiceUrl).build();
    }

    @Bean
    public WebClient guestGamesServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl(guestGameServiceUrl).build();
    }
}
