package com.example.demo.tests;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindUserByUsername() {
        String username = UUID.randomUUID().toString();
        User user = new User(username, "test@example.com","hashedPassword");
        userRepository.save(user);


        Optional<User> foundUser = userRepository.findByUsername(username);

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(user.getId(), foundUser.get().getId());
        Assertions.assertEquals(username, foundUser.get().getUsername());

    }
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testRemoveUserByID()
    {
        String username = UUID.randomUUID().toString();
        User user = new User(username, UUID.randomUUID().toString(),"hashedPassword");
        userRepository.save(user);

        userRepository.deleteById(user.getId());

        Optional<User> foundUser = userRepository.findByUsername(username);

        Assertions.assertFalse(foundUser.isPresent());
    }

}