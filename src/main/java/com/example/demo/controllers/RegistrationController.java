package com.example.demo.controllers;

import com.example.demo.constants.ViewConstants;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AlreadyExistException;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Контроллер для обработки регистрации пользователей.
 * <p>
 * Обрабатывает запросы, связанные с отображением формы регистрации
 * и созданием нового пользователя.
 *
 * @author VladimirBoss
 */
@Controller
public class RegistrationController {

    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    @Autowired
    private UserService userService;

    /**
     * Отображает форму регистрации.
     *
     * @param model Модель для передачи данных в представление.
     * @return Название HTML-шаблона для формы регистрации.
     */
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("error", "");
        logger.debug("Отображение формы регистрации.");
        return ViewConstants.REGISTRATION_VIEW;
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            userService.createUser(user.getUsername(), user.getEmail(), user.getPassword());
            logger.info("Пользователь {} успешно зарегистрирован.", user.getUsername());
        } catch (AlreadyExistException e) {
            model.addAttribute("error", e.getMessage());
            logger.warn("Попытка зарегистрировать существующего пользователя: UserName: {}, Email: {}", user.getUsername(), user.getEmail());
            return ViewConstants.REGISTRATION_VIEW;
        }
        return "redirect:/" + ViewConstants.LOGIN_VIEW;
    }
}