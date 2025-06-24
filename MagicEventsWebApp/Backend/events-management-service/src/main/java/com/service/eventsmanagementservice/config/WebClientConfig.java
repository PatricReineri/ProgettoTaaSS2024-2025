package com.service.eventsmanagementservice.config;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;

@Configuration
public class WebClientConfig {
    @Value("${services.usermanagement.url}")
    private String userManagementUrl;

    @Bean
    public WebClient userManagementWebClient() {
        HttpClient httpClient = HttpClient.create()
                .secure(spec -> {
                    try {
                        spec.sslContext(SslContextBuilder
                                .forClient()
                                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                .build()
                        );
                    } catch (SSLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .responseTimeout(Duration.ofSeconds(30));

        return WebClient.builder()
                .baseUrl(userManagementUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(logRequest())
                .filter(logResponse())
                .codecs(c -> c.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(req -> {
            System.out.println("Request: " + req.method() + " " + req.url());
            return Mono.just(req);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(res -> {
            System.out.println("Response status: " + res.statusCode());
            return Mono.just(res);
        });
    }
}