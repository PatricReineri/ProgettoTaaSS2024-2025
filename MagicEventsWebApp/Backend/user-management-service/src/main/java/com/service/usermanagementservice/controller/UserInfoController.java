package com.service.usermanagementservice.controller;

import com.service.usermanagementservice.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/info")
public class UserInfoController {
    private final UserService userService;

    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public HashMap<Long, String> getUserEmail(@RequestParam("email") List<String> email) throws Exception {
        return userService.getUserEmail(email);
    }

    @PostMapping("/isauthenticated")
    public Boolean isAuthenticated(@RequestParam("email") String email) throws Exception {
        return userService.isAuthenticated(email);
    }
}

