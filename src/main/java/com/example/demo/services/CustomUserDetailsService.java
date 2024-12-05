package com.example.demo.services;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Пользовательский сервис для загрузки данных о пользователе.
 * <p>
 * Этот класс реализует интерфейс UserDetailsService и предоставляет
 * метод для загрузки пользователя по имени пользователя.
 * </p>
 *
 * @author VladimirBoss
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загружает данные о пользователе по имени пользователя.
     *
     * @param username Имя пользователя, для которого нужно загрузить данные.
     * @return UserDetails объект, содержащий информацию о пользователе.
     * @throws UsernameNotFoundException Если пользователь не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
        userBuilder.password(user.getPassword());
        userBuilder.roles(getRoles(user));
        return userBuilder.build();
    }

    /**
     * Получает роли пользователя в виде массива строк.
     *
     * @param user Пользователь, для которого нужно получить роли.
     * @return Массив строк, представляющий роли пользователя.
     */
    private String[] getRoles(User user) {

        return user.getRoles().stream().map(Role::getName).toArray(String[]::new);

    }
}