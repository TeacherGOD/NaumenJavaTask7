package com.example.demo.controllers;

import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для управления ролями пользователей.
 * <p>
 * Обрабатывает запросы, связанные с получением ролей аутентифицированного пользователя.
 *
 * @author VladimirBoss
 */
@RestController
public class RoleController {
    private final Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private UserService userService;

    /**
     * Возвращает текущие роли аутентифицированного пользователя в формате JSON.
     *
     * @param userDetails Данные аутентифицированного пользователя.
     * @return ResponseEntity с ролями пользователя.
     */
    @GetMapping("/currentUserRoles")
    public ResponseEntity<List<String>> currentUserRoles(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        logger.info("Пользователь {} имеет следующие роли: {}", userDetails.getUsername(), roles);
        return ResponseEntity.ok(roles);
    }
}
