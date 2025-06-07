package com.service.usermanagementservice.controller;

import com.service.usermanagementservice.dto.LoginDTO;
import com.service.usermanagementservice.dto.UserDTO;
import com.service.usermanagementservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginDTO request) {
        UserDTO loginResponse = authService.authenticate(request);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @GetMapping("/google")
    public ResponseEntity<UserDTO> loginWithGoogle() {
        UserDTO googleAuthUrl = authService.authenticateWithGoogle();
        return ResponseEntity.ok(googleAuthUrl);
    }

    @GetMapping("/test")
    public String test() {
        return "Hello World!";
    }
}
