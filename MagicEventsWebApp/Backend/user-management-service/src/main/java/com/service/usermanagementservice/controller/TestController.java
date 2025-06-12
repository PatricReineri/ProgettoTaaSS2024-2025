package com.service.usermanagementservice.controller;

import com.service.usermanagementservice.model.OauthToken;
import com.service.usermanagementservice.model.User;
import com.service.usermanagementservice.repository.OauthTokenRepository;
import com.service.usermanagementservice.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    private final UserRepository userRepository;
    private final OauthTokenRepository oauthTokenRepository;

    public TestController(UserRepository userRepository, OauthTokenRepository oauthTokenRepository) {
        this.userRepository = userRepository;
        this.oauthTokenRepository = oauthTokenRepository;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/tokens")
    public List<OauthToken> getTokens() {
        return oauthTokenRepository.findAll();
    }

     @GetMapping
     public String test() {
         return "Hello World!";
     }
}
