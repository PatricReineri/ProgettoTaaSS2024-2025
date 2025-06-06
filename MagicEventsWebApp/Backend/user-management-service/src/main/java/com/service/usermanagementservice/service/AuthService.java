package com.service.usermanagementservice.service;

import com.service.usermanagementservice.dto.LoginDTO;
import com.service.usermanagementservice.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    public UserDTO authenticate(@Valid LoginDTO request) { return new UserDTO(); }

    private UserDTO _authenticate(@Valid LoginDTO request) { return new UserDTO(); }

    public UserDTO authenticateWithGoogle() { return new UserDTO(); }

    private UserDTO _authenticateWithGoogle() { return new UserDTO(); }
}
