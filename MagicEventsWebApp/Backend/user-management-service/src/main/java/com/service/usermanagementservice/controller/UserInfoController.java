package com.service.usermanagementservice.controller;

import com.service.usermanagementservice.dto.UserDTO;
import com.service.usermanagementservice.service.AuthService;
import com.service.usermanagementservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/info")
public class UserInfoController {
    private final AuthService authService;
    private final UserService userService;

    public UserInfoController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<HashMap<Long, String>> getUserEmail(
            @RequestBody List<String> emails
    ) throws Exception {
        HashMap<Long, String> result = userService.getUserEmail(emails);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/profile")
    public UserDTO userProfile(@RequestParam("magicEventTag") Long id) throws Exception {
        return userService.userProfile(id);
    }

    @PutMapping("/logoutuser")
    public String logoutUser(@RequestParam("email") String email) {
        return authService.logout(email);
    }

    @DeleteMapping("/deleteuser")
    public String deleteUser(@RequestParam("email") String email) {
        return authService.deleteUser(email);
    }

    @PutMapping("/modifyuser")
    public UserDTO modifyUser(@Valid @RequestBody UserDTO userDTO) {
        return authService.modify(userDTO);
    }
}

