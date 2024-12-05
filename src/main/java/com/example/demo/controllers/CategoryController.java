package com.example.demo.controllers;

import com.example.demo.constants.ViewConstants;
import com.example.demo.entities.Category;
import com.example.demo.entities.User;
import com.example.demo.services.CategoryService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 * Контроллер для управления категориями пользователей.
 * <p>
 * Обрабатывает запросы, связанные с отображением, добавлением и удалением категорий.
 *
 * @author VladimirBoss
 * @see CategoryService
 * @see UserService
 */
@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;


    /**
     * Отображает все категории пользователя.
     *
     * @param userDetails Данные аутентифицированного пользователя.
     * @param model       Модель для передачи данных в представление.
     * @return Название HTML-шаблона для отображения категорий.
     */
    @GetMapping
    public String getCategories(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        var categories = categoryService.getCategoriesByUser(user);
        model.addAttribute("categories", categories);
        return ViewConstants.CATEGORY_VIEW;
    }

    /**
     * Добавляет новую категорию.
     *
     * @param name        Название категории.
     * @param parentId    ID родительской категории (может быть null).
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Перенаправление на страницу категорий.
     */
    @PostMapping("/add")
    public String addCategory(@RequestParam String name,
                              @RequestParam(required = false) Long parentId,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Category parentCategory = (parentId != null) ? categoryService.getCategoryById(parentId) : null;
        categoryService.createCategory(name, user, parentCategory);
        return "redirect:/" + ViewConstants.CATEGORY_VIEW;
    }

    /**
     * Удаляет категорию по ID.
     *
     * @param id          ID категории для удаления.
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Перенаправление на страницу категорий.
     */
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        categoryService.deleteCategoryForUser(id, user);
        return "redirect:/" + ViewConstants.CATEGORY_VIEW;
    }

    /**
     * Обновляет название категории по ID.
     *
     * @param id          ID категории для изменения.
     * @param newName     Новое название категории.
     */
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, @RequestParam String newName, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        categoryService.updateCategoryForUser(id, newName, user);
        return "redirect:/" + ViewConstants.CATEGORY_VIEW;
    }
}