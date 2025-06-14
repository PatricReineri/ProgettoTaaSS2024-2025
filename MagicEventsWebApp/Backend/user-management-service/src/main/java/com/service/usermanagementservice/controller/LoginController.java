package com.service.usermanagementservice.controller;

import com.service.usermanagementservice.dto.LoginWithTokenDTO;
import com.service.usermanagementservice.dto.UserDTO;
import com.service.usermanagementservice.service.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        return authService.registerUser(name, surname, email, password, username);
    }

    @PostMapping("/form")
    public UserDTO login(@RequestParam("email") String email, @RequestParam("password") String password) {
        return authService.login(email, password);
    }

    @GetMapping("/grantcode")
    public UserDTO grantCode(@RequestParam("code") String code) {
        return authService.processGrantCode(code);
    }

    @PutMapping("/logoutuser")
    public String logoutUser(@RequestParam("email") String email) {
        //String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return authService.logout(email);
    }

    @PostMapping("/refreshaccesstoken")
    public LoginWithTokenDTO refreshAccessToken(@RequestParam("refresh_token") String refreshToken) {
        return authService.refreshAccessToken(refreshToken);
    }
}
