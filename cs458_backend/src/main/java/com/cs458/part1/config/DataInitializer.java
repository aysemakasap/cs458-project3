package com.cs458.part1.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs458.part1.model.Users;
import com.cs458.part1.repository.UsersRepository;

import java.time.LocalDate;

@Component
public class DataInitializer {

    private final UsersRepository usersRepository;

    @Autowired
    public DataInitializer(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @PostConstruct
    public void initData() {
        // Check if we already have users
        if (usersRepository.count() == 0) {
            System.out.println("Initializing database with test user...");
            
            // Create a test user
            Users testUser = new Users();
            testUser.setId(1);
            testUser.setName("Test");
            testUser.setSurname("User");
            testUser.setEmail("test@example.com");
            testUser.setPassword("password123");  // In a real app, this would be hashed
            testUser.setPhoneNumber("1234567890");
            testUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
            
            usersRepository.save(testUser);
            
            System.out.println("Test user created successfully!");
        }
    }
} 