package com.cs458.part1.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cs458.part1.exception.UserNotFoundException;
import com.cs458.part1.model.Users;
import com.cs458.part1.repository.UsersRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UserService userService;

    private Users user;

    @BeforeEach
    public void setup() {
        user = new Users();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setSurname("User");
        user.setPhoneNumber("1234567890");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
    }

    @Test
    public void shouldGetAllUsers() {
        // Given
        List<Users> usersList = new ArrayList<>();
        usersList.add(user);
        
        Users user2 = new Users();
        user2.setId(2);
        user2.setEmail("test2@example.com");
        usersList.add(user2);
        
        given(usersRepository.findAll()).willReturn(usersList);

        // When
        List<Users> result = userService.getAllUsers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    public void shouldGetUserById() {
        // Given
        given(usersRepository.findById(1)).willReturn(Optional.of(user));

        // When
        Users found = userService.getUserById(1);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("test@example.com");
        verify(usersRepository, times(1)).findById(1);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        // Given
        given(usersRepository.findById(999)).willReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(999);
        });
        
        verify(usersRepository, times(1)).findById(999);
    }

    @Test
    public void shouldCreateUser() {
        // Given
        given(usersRepository.save(any(Users.class))).willReturn(user);

        // When
        Users created = userService.createUser(user);

        // Then
        assertThat(created).isNotNull();
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    public void shouldUpdateUser() {
        // Given
        given(usersRepository.findById(1)).willReturn(Optional.of(user));
        given(usersRepository.save(any(Users.class))).willReturn(user);
        
        user.setEmail("updated@example.com");

        // When
        Users updated = userService.updateUser(1, user);

        // Then
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");
        verify(usersRepository, times(1)).findById(1);
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        // Given
        given(usersRepository.findById(999)).willReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(999, user);
        });
        
        verify(usersRepository, times(1)).findById(999);
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    public void shouldDeleteUser() {
        // Given
        given(usersRepository.findById(1)).willReturn(Optional.of(user));
        doNothing().when(usersRepository).deleteById(1);

        // When
        userService.deleteUser(1);

        // Then
        verify(usersRepository, times(1)).findById(1);
        verify(usersRepository, times(1)).deleteById(1);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingNonExistentUser() {
        // Given
        given(usersRepository.findById(999)).willReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(999);
        });
        
        verify(usersRepository, times(1)).findById(999);
        verify(usersRepository, never()).deleteById(anyInt());
    }
} 