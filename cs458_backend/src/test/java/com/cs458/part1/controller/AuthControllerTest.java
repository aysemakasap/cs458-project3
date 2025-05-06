package com.cs458.part1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cs458.part1.dto.LoginRequest;
import com.cs458.part1.exception.AuthenticationException;
import com.cs458.part1.model.Users;
import com.cs458.part1.service.AuthService;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private Users user;
    private LoginRequest loginRequest;

    @BeforeEach
    public void setup() {
        user = new Users();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setSurname("User");
        user.setPassword("password123");
        user.setPhoneNumber("1234567890");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    public void shouldLoginSuccessfully() throws Exception {
        given(authService.login("test@example.com", "password123")).willReturn(user);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void shouldReturn401WhenUserNotFound() throws Exception {
        given(authService.login(anyString(), anyString())).willThrow(new AuthenticationException("User not found"));

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturn401WhenPasswordIncorrect() throws Exception {
        loginRequest.setPassword("wrongpassword");
        given(authService.login("test@example.com", "wrongpassword")).willThrow(new AuthenticationException("Invalid password"));

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
} 