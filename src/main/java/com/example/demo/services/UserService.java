package com.example.demo.services;

import com.example.demo.entities.Role;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AlreadyExistException;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.TagRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TagRepository tagRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TagRepository tagRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public User createUser(String username, String email, String password) {

        if (userRepository.existsByUsername(username)) {
            throw new AlreadyExistException("Username already exists: " + username);
        }

        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistException("Email already exists: " + email);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        var resUser=userRepository.save(user);
        addDefaultTags(user);
        return resUser;
        //return userRepository.save(user);
    }

    public void addRole(User user,String role) {
        Role userRole = roleRepository.findByName(role)
               .orElseThrow(() -> new RuntimeException("Role " + role + " not found"));
        user.getRoles().add(userRole);
        userRepository.save(user);
    }


    private void addDefaultTags(User user) {
        List<String> defaultTagNames = Arrays.asList("Срочное", "Личное", "Работа"); // Предустановленные теги
        for (String tagName : defaultTagNames) {
            Tag tag = new Tag(tagName, user);
            tagRepository.save(tag);
        }
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
                user.setPassword(password);
            }
            userRepository.save(user);
        }
    }

    public void upgradeToPremium(User user) {
        addRole(user,"PREMIUM");
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}