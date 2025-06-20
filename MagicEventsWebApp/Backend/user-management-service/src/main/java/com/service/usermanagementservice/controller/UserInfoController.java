package com.service.usermanagementservice.controller;

import com.service.usermanagementservice.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/info")
public class UserInfoController {
    private final UserService userService;

    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public List<Long> getUserEmail(@RequestParam("email") List<String> email) throws Exception {
        return userService.getUserEmail(email);
    }
}

