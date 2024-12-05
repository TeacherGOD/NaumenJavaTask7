package com.example.demo.controllers;

import com.example.demo.constants.ViewConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для обработки запросов на страницу входа.
 * <p>
 * Обрабатывает запросы к конечной точке "/login" и возвращает
 * соответствующий HTML-шаблон.
 *
 * @author VladimirBoss
 */
@Controller
public class LoginController {

    /**
     * Отображает страницу входа.
     *
     * @return Название HTML-шаблона для страницы входа.
     */
    @GetMapping("/login")
    public String login() {
        return ViewConstants.LOGIN_VIEW;
    }

}