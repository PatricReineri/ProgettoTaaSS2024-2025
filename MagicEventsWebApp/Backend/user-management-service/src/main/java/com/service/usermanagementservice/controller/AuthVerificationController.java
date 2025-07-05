package com.service.usermanagementservice.controller;

import com.service.usermanagementservice.model.OauthToken;
import com.service.usermanagementservice.model.User;
import com.service.usermanagementservice.repository.OauthTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthVerificationController {

    private final OauthTokenRepository oauthTokenRepository;

    public AuthVerificationController(OauthTokenRepository oauthTokenRepository) {
        this.oauthTokenRepository = oauthTokenRepository;
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verifyToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.startsWith("Bearer")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        OauthToken oauthToken = oauthTokenRepository.findById(token).orElse(null);

        if (oauthToken == null || oauthToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = oauthToken.getUser();

        response.setHeader("X-User-Email", user.getEmail());
        response.setHeader("X-User-Role", user.getRole());
        response.setHeader("X-User-ID", user.getMagicEventTag().toString());
        response.setHeader("X-Username", user.getUsername());

        return ResponseEntity.ok().build();
    }
}
