package com.example.demo.tests;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // Используем SpringBootTest для загрузки полного контекста приложения
@ActiveProfiles("test") // Используем профиль test
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;


    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "test@example.com", "password");
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    void testFindByUsername() {
        Optional<User> foundUser = userRepository.findByUsername("testUser");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testUser");
    }

    @Test
    void testFindUserByEmail() {
        Optional<User> foundUser = userRepository.findUserByEmail("test@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindByNonExistentUsername() {
        Optional<User> foundUser = userRepository.findByUsername("nonExistentUser");
        assertThat(foundUser).isEmpty();
    }

    @Test
    void testFindUserByNonExistentEmail() {
        Optional<User> foundUser = userRepository.findUserByEmail("nonExistent@example.com");
        assertThat(foundUser).isEmpty();
    }

    @Test
    void testSaveUser() {
        User newUser = new User("newUser", "new@example.com", "newPassword");
        User savedUser = userRepository.save(newUser);
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(savedUser.getEmail()).isEqualTo(newUser.getEmail());
    }
}