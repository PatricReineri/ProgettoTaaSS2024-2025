package com.service.usermanagementservice.config;

import com.service.usermanagementservice.model.OauthToken;
import com.service.usermanagementservice.model.User;
import com.service.usermanagementservice.repository.OauthTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.service.usermanagementservice.config.BearerTokenAuth;

import java.io.IOException;
import java.time.LocalDateTime;
@Component
public class BearerTokenAuthFilter extends OncePerRequestFilter {

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ") && !authHeader.substring(7).isBlank()) {
            String bearerToken = authHeader.substring(7);
            Authentication bearerTokenAuthentication = new BearerTokenAuth(bearerToken);

            try {
                Authentication authentication = authenticationManager.authenticate(bearerTokenAuthentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (AuthenticationException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}