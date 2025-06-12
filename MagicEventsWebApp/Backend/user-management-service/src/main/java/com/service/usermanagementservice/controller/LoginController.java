package com.service.usermanagementservice.controller;

import com.service.usermanagementservice.dto.LoginWithTokenDTO;
import com.service.usermanagementservice.service.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    /*
    @PostMapping("/register")
    public String register(@RequestParam("firstName") String firstName, @RequestParam("password") String password, @RequestParam("lastName") String lastName, @RequestParam("email") String email ) {
        authService.registerUser(firstName, lastName, email, password);
        return "User registered successfully!";
    }

    @PostMapping
    public LoginWithTokenDTO login(@RequestParam String username, @RequestParam String password) {
        return authService.login(username, password);
    }
    */

    @GetMapping("/grantcode")
    public LoginWithTokenDTO grantCode(
            @RequestParam("code") String code
    ) {
        return authService.processGrantCode(code);
    }

    @PutMapping("/logoutuser")
    public String logoutUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return authService.logout(email);
    }

    @PostMapping("/refreshaccesstoken")
    public LoginWithTokenDTO refreshAccessToken(@RequestParam("refresh_token") String refreshToken) {
        return authService.refreshAccessToken(refreshToken);
    }
}
