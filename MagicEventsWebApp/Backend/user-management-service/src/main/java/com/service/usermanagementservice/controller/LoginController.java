package com.service.usermanagementservice.controller;

import com.service.usermanagementservice.dto.LoginWithTokenDTO;
import com.service.usermanagementservice.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    // TODO: login with form
    /*
    @PostMapping
    public LoginWithGoogleDTO login(@RequestParam String username, @RequestParam String password) {
        return authService.login(username, password);
    }*/

    @GetMapping("/grantcode")
    public LoginWithTokenDTO grantCode(
            @RequestParam("code") String code,
            @RequestParam("scope") String scope,
            @RequestParam("authuser") String authUser,
            @RequestParam("prompt") String prompt
    ) {
        return authService.processGrantCode(code);
    }

    /**
     * TODO
     * @PostMapping
     * public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginDTO request) { }
     */
}
