package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(password);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void updateUser(Long id, String username, String email, String password) {
        User user = getUserById(id);
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);
            if (password != null && !password.isEmpty()) {
                user.setPasswordHash(password);
            }
            userRepository.save(user);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}