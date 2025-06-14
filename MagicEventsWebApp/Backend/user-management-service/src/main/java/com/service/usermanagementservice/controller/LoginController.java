package com.service.usermanagementservice.controller;

import com.service.usermanagementservice.dto.LoginWithTokenDTO;
import com.service.usermanagementservice.dto.UserDTO;
import com.service.usermanagementservice.model.OauthToken;
import com.service.usermanagementservice.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @PostMapping("/generateresetpasswordlink")
    public String generateResetPasswordLink(@RequestParam String email) {
        return authService.initiateResetPasswordLink(email);
    }

    @PutMapping("/changepassword")
    public String changePassword(@RequestParam("token") String token, @RequestParam("new_password") String newPassword) {
        return authService.changePasswordWithToken(token, newPassword);
    }

    @PostMapping("/userprofile")
    public UserDTO getUserInfo(@RequestParam("accessToken") String accessToken) {
        return authService.getUserInfo(accessToken);
    }

    @GetMapping("/grantcode")
    public void grantCode(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        LoginWithTokenDTO oauthToken = authService.processGrantCode(code);
        response.sendRedirect("https://localhost:3000/googlecallback?accessToken=" + oauthToken.getAccessToken());
    }

    @PutMapping("/logoutuser")
    public String logoutUser(@RequestParam("email") String email) {
        return authService.logout(email);
    }

    @PostMapping("/refreshaccesstoken")
    public LoginWithTokenDTO refreshAccessToken(@RequestParam("refresh_token") String refreshToken) {
        return authService.refreshAccessToken(refreshToken);
    }
}
