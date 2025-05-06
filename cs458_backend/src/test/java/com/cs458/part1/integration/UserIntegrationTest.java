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

import com.cs458.part1.model.Users;
import com.cs458.part1.repository.UsersRepository;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    void setup() {
        usersRepository.deleteAll();
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnSavedUser() throws Exception {
        // Given
        Users user = new Users();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setSurname("User");
        user.setPhoneNumber("1234567890");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));

        // When
        ResultActions response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // Then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void givenListOfUsers_whenGetAllUsers_thenReturnUsersList() throws Exception {
        // Given
        Users user1 = new Users();
        user1.setId(1);
        user1.setEmail("test1@example.com");
        user1.setName("Test1");
        user1.setSurname("User1");
        user1.setPhoneNumber("1234567890");
        user1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        
        Users user2 = new Users();
        user2.setId(2);
        user2.setEmail("test2@example.com");
        user2.setName("Test2");
        user2.setSurname("User2");
        user2.setPhoneNumber("0987654321");
        user2.setDateOfBirth(LocalDate.of(1995, 5, 5));
        
        usersRepository.save(user1);
        usersRepository.save(user2);

        // When
        ResultActions response = mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    public void givenUserId_whenGetUserById_thenReturnUserObject() throws Exception {
        // Given
        Users user = new Users();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setSurname("User");
        user.setPhoneNumber("1234567890");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        
        usersRepository.save(user);

        // When
        ResultActions response = mockMvc.perform(get("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void givenUpdatedUser_whenUpdateUser_thenReturnUpdatedUserObject() throws Exception {
        // Given
        Users user = new Users();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setSurname("User");
        user.setPhoneNumber("1234567890");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        
        usersRepository.save(user);

        Users updatedUser = new Users();
        updatedUser.setId(1);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setName("Updated");
        updatedUser.setSurname("User");
        updatedUser.setPhoneNumber("9876543210");
        updatedUser.setDateOfBirth(LocalDate.of(1991, 2, 2));

        // When
        ResultActions response = mockMvc.perform(put("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedUser.getName())))
                .andExpect(jsonPath("$.email", is(updatedUser.getEmail())))
                .andExpect(jsonPath("$.phoneNumber", is(updatedUser.getPhoneNumber())));
    }

    @Test
    public void givenUserId_whenDeleteUser_thenReturn204() throws Exception {
        // Given
        Users user = new Users();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setSurname("User");
        user.setPhoneNumber("1234567890");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        
        usersRepository.save(user);

        // When
        ResultActions response = mockMvc.perform(delete("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isNoContent());
        
        // Verify user was deleted
        mockMvc.perform(get("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
} 