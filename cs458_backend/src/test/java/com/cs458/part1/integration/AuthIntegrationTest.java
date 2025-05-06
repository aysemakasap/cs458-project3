package com.cs458.part1.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.cs458.part1.dto.LoginRequest;
import com.cs458.part1.model.Users;
import com.cs458.part1.repository.UsersRepository;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsersRepository usersRepository;

    private Users user;
    private LoginRequest loginRequest;

    @BeforeEach
    void setup() {
        usersRepository.deleteAll();

        // Create a test user
        user = new Users();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setSurname("User");
        user.setPassword("password123");
        user.setPhoneNumber("1234567890");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        
        usersRepository.save(user);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    public void givenValidCredentials_whenLogin_thenReturnUser() throws Exception {
        // When
        ResultActions response = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void givenInvalidEmail_whenLogin_thenReturn401() throws Exception {
        // Given
        loginRequest.setEmail("nonexistent@example.com");

        // When
        ResultActions response = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        // Then
        response.andExpect(status().isUnauthorized());
    }

    @Test
    public void givenInvalidPassword_whenLogin_thenReturn401() throws Exception {
        // Given
        loginRequest.setPassword("wrongpassword");

        // When
        ResultActions response = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        // Then
        response.andExpect(status().isUnauthorized());
    }
} 