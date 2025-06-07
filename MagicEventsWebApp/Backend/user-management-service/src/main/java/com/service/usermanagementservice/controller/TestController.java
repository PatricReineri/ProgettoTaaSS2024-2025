package com.service.usermanagementservice.controller;

import com.service.usermanagementservice.model.User;
import com.service.usermanagementservice.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

     @GetMapping
     public String test() {
         return "Hello World!";
     }
}
