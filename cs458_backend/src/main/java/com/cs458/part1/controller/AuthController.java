package com.cs458.part1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cs458.part1.dto.LoginRequest;
import com.cs458.part1.exception.AuthenticationException;
import com.cs458.part1.model.Users;
import com.cs458.part1.service.AuthService;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Users> login(@RequestBody LoginRequest loginRequest) {
        Users authenticatedUser = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
} 