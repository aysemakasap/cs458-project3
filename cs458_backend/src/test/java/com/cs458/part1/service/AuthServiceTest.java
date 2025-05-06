package com.cs458.part1.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cs458.part1.exception.AuthenticationException;
import com.cs458.part1.model.Users;
import com.cs458.part1.repository.UsersRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private AuthService authService;

    private Users user;

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
    }

    @Test
    public void shouldLoginSuccessfully() {
        // Given
        List<Users> userList = new ArrayList<>();
        userList.add(user);
        given(usersRepository.findAll()).willReturn(userList);

        // When
        Users loggedInUser = authService.login("test@example.com", "password123");

        // Then
        assertThat(loggedInUser).isNotNull();
        assertThat(loggedInUser.getEmail()).isEqualTo("test@example.com");
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        // Given
        List<Users> userList = new ArrayList<>();
        given(usersRepository.findAll()).willReturn(userList);

        // When & Then
        assertThrows(AuthenticationException.class, () -> {
            authService.login("nonexistent@example.com", "password123");
        });
        
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    public void shouldThrowExceptionWhenPasswordIncorrect() {
        // Given
        List<Users> userList = new ArrayList<>();
        userList.add(user);
        given(usersRepository.findAll()).willReturn(userList);

        // When & Then
        assertThrows(AuthenticationException.class, () -> {
            authService.login("test@example.com", "wrongpassword");
        });
        
        verify(usersRepository, times(1)).findAll();
    }
} 