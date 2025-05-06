package com.cs458.part1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs458.part1.exception.AuthenticationException;
import com.cs458.part1.model.Users;
import com.cs458.part1.repository.UsersRepository;

import java.util.List;

@Service
public class AuthService {

    private final UsersRepository usersRepository;

    @Autowired
    public AuthService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users login(String email, String password) {
        // Find users with the given email
        List<Users> users = usersRepository.findAll().stream()
                .filter(user -> email.equals(user.getEmail()))
                .toList();

        if (users.isEmpty()) {
            throw new AuthenticationException("User not found with email: " + email);
        }

        Users user = users.get(0);
        
        // Check if password matches
        if (!password.equals(user.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        return user;
    }
} 