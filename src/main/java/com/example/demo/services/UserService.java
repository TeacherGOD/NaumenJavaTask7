package com.example.demo.services;

import com.example.demo.entities.Role;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AlreadyExistException;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.TagRepository;
import com.example.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Сервис для управления пользователями.
 * <p>
 * Этот класс предоставляет методы для создания, обновления,
 * удаления пользователей, а также управления их ролями и тегами.
 * </p>
 *
 * @author VladimirBoss
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService, CategoryService categoryService, TagService tagService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    /**
     * Создание нового пользователя.
     *
     * @param username Имя пользователя.
     * @param email Адрес электронной почты пользователя.
     * @param password Пароль пользователя.
     * @return Созданный пользователь.
     * @throws AlreadyExistException Если имя пользователя или адрес электронной почты уже существуют.
     */
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
        this.addRole(user,"USER");
        var resUser=userRepository.save(user);
        addDefaultTags(user);
        addDefaultCategories(user);
        logger.info("Created new user with username: {}", username);
        return resUser;
    }

    /**
     * Добавление предустановленных категорий для пользователя.
     *
     * @param user Пользователь, для которого будут добавлены категории.
     */
    private void addDefaultCategories(User user) {
        List<String> defaultCategoryNames = Arrays.asList("Работа", "Личное", "Домашние","Мысли");
        for (String categoryName : defaultCategoryNames) {
            categoryService.createCategory(categoryName,user,null);
            logger.debug("Added default category '{}' for user: {}", categoryName, user.getUsername());
        }
    }

    /**
     * Добавление роли пользователю.
     *
     * @param user Пользователь, которому будет добавлена роль.
     * @param role Название роли для добавления.
     */
    @Transactional
    public void addRole(User user,String role) {
        Role userRole = roleRepository.findByName(role)
               .orElseThrow(() -> new RuntimeException("Role " + role + " not found"));
        user.getRoles().add(userRole);
        userRepository.save(user);
        logger.info("Added role '{}' to user: {}", role, user.getUsername());
    }


    /**
     * Добавление предустановленных тегов для пользователя.
     *
     * @param user Пользователь, для которого будут добавлены теги.
     */
    private void addDefaultTags(User user) {
        List<String> defaultTagNames = Arrays.asList("Срочное", "Личное", "Работа"); // Предустановленные теги
        for (String tagName : defaultTagNames) {
            tagService.createTag(tagName,user);
            logger.debug("Added default tag '{}' for user: {}", tagName, user.getUsername());
        }
    }

    /**
     * Получение всех пользователей.
     *
     * @return Список всех пользователей.
     */
    public List<User> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .toList();
    }

    /**
     * Получение пользователя по ID.
     *
     * @param id ID пользователя.
     * @return Найденный пользователь или null, если не найден.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Обновление информации о пользователе.
     *
     * @param id ID пользователя для обновления.
     * @param username Новое имя пользователя.
     * @param email Новый адрес электронной почты пользователя.
     * @param password Новый пароль пользователя (может быть null).
     */
    public void updateUser(Long id, String username, String email, String password) {
        User user = getUserById(id);
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);
            if (password != null && !password.isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }
            userRepository.save(user);
            logger.info("Updated information for user with ID: {}", id);
        } else {
            logger.warn("Attempted to update non-existent user with ID: {}", id);
        }
    }

    /**
     * Обновление роли пользователя на PREMIUM.
     *
     * @param user Пользователь для обновления роли.
     */
    @Transactional
    public void upgradeToPremium(User user) {
        String role = "PREMIUM";
        if (!user.hasRole(role)) {
            addRole(user,role);
        }
        //обновление контекста безопасности, чтобы тег добавился в роли UserDetails.
        UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(updatedUserDetails, null, updatedUserDetails.getAuthorities())
        );
        logger.info("Upgraded user with ID: {} to PREMIUM role", user.getId());
    }

    /**
     * Удаление пользователя по ID.
     *
     * @param id ID пользователя для удаления.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Поиск пользователя по имени пользователя.
     *
     * @param username Имя пользователя для поиска.
     * @return Найденный пользователь или null, если не найден.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}