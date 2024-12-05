package com.example.demo.controllers;

import com.example.demo.constants.ViewConstants;
import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для управления пользователями.
 * <p>
 * Обрабатывает запросы, связанные с действиями пользователей,
 * такими как повышение до премиум-аккаунта.
 * </p>
 *
 * @author VladimirBoss
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод для повышения пользователя до премиум-аккаунта.
     *
     * @param id ID пользователя, которого нужно повысить.
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Перенаправление на страницу со всеми заметками.
     */
    @PostMapping("/{id}/upgradeToPremium")
    public String upgradeToPremium(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User authUser = userService.findByUsername(userDetails.getUsername());
        User user = userService.getUserById(id);
        if (user != null) {
            if (authUser.hasRole("ADMIN") || user.equals(authUser)) {
                userService.upgradeToPremium(user);
                logger.info("Пользователь {} был повышен до премиум-аккаунта.", user.getUsername());
            }
            else {
                logger.warn("Пользователь {} не имеет прав для повышения аккаунта пользователя {}.", authUser.getUsername(), user.getUsername());
            }
        }else {
            logger.error("Пользователь с ID {} не найден.", id);
        }
        return "redirect:"+ ViewConstants.HOME_PAGE;
    }
}