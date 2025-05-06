package com.cs458.part1.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.cs458.part1.model.Users;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UsersRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    public void shouldSaveUser() {
        // Given
        Users user = new Users();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setSurname("User");
        user.setPhoneNumber("1234567890");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));

        // When
        Users savedUser = usersRepository.save(user);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(1);
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void shouldFindUserById() {
        // Given
        Users user = new Users();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setSurname("User");
        user.setPhoneNumber("1234567890");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        
        entityManager.persist(user);
        entityManager.flush();

        // When
        Optional<Users> found = usersRepository.findById(user.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(user.getName());
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void shouldFindAllUsers() {
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
        
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // When
        List<Users> usersList = usersRepository.findAll();

        // Then
        assertThat(usersList).hasSize(2);
    }

    @Test
    public void shouldDeleteUser() {
        // Given
        Users user = new Users();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setName("Test");
        user.setSurname("User");
        user.setPhoneNumber("1234567890");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        
        entityManager.persist(user);
        entityManager.flush();

        // When
        usersRepository.deleteById(user.getId());

        // Then
        Optional<Users> deleted = usersRepository.findById(user.getId());
        assertThat(deleted).isEmpty();
    }
} 