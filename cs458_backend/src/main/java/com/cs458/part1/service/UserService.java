package com.cs458.part1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs458.part1.exception.UserNotFoundException;
import com.cs458.part1.exception.UserRegistrationException;
import com.cs458.part1.model.Users;
import com.cs458.part1.repository.UsersRepository;

import java.util.List;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users getUserById(Integer id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public Users createUser(Users user) {
        return usersRepository.save(user);
    }

    public Users registerUser(Users user) {
        // Check if email already exists
        boolean emailExists = usersRepository.findAll().stream()
                .anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()));
        
        if (emailExists) {
            throw new UserRegistrationException("Email already in use: " + user.getEmail());
        }
        
        // Additional validations could be added here
        
        return usersRepository.save(user);
    }

    public Users updateUser(Integer id, Users userDetails) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setName(userDetails.getName());
        user.setSurname(userDetails.getSurname());
        user.setEmail(userDetails.getEmail());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setDateOfBirth(userDetails.getDateOfBirth());
        
        // Only update password if it's provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(userDetails.getPassword());
        }

        return usersRepository.save(user);
    }

    public void deleteUser(Integer id) {
        // Check if user exists first
        usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        usersRepository.deleteById(id);
    }
} 