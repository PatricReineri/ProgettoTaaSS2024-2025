package com.service.usermanagementservice.controller;

import com.service.usermanagementservice.dto.UserDTO;
import com.service.usermanagementservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<HashMap<Long, String>> getUserEmail(
            @RequestBody List<String> emails
    ) throws Exception {
        HashMap<Long, String> result = userService.getUserEmail(emails);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/isauthenticated")
    public Boolean isAuthenticated(@RequestParam("email") String email) throws Exception {
        return userService.isAuthenticated(email);
    }

    @GetMapping("/profile")
    public UserDTO userProfile(@RequestParam("magicEventTag") Long id) throws Exception {
        return userService.userProfile(id);
    }
}

