package com.cs458.part1.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
} 