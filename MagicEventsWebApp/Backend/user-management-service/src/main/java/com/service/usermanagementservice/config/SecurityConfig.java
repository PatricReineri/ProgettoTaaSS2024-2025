package com.service.usermanagementservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    BearerTokenAuthFilter bearerTokenAuthFilter;

    private static final String[] WHITELIST_URLS = {
            "/login",
            "/login/register",
            "/login/grantcode",
            "/login/logoutuser",
            "/login/refreshaccesstoken",
            "/test",
            "/test/users",
            "/test/tokens"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(WHITELIST_URLS).permitAll();
                    request.anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(bearerTokenAuthFilter, BasicAuthenticationFilter.class);
        return http.build();
    }
}

