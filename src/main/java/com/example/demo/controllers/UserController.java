package com.example.demo.controllers;

import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Метод для повышения до премиум-аккаунта
    @PostMapping("/{id}/upgradeToPremium")
    public String upgradeToPremium(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User authUser=userService.findByUsername(userDetails.getUsername());
        User user = userService.getUserById(id);
        if (user != null) {
            if (authUser.hasRole("ADMIN")|| user.equals(authUser)) {
                userService.upgradeToPremium(user);
            }
        }
        return "redirect:/notes/view/all"; // Перенаправление на страницу пользователей
    }
}