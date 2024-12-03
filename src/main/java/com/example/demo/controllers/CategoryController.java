package com.example.demo.controllers;

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

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;


    // Отображение всех категорий пользователя
    @GetMapping
    public String getCategories(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        var categories = categoryService.getCategoriesByUser(user);
        model.addAttribute("categories", categories);
        return "categories"; // название HTML-шаблона
    }

    // Добавление новой категории
    @PostMapping("/add")
    public String addCategory(@RequestParam String name,
                              @RequestParam(required = false) Long parentId,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Category parentCategory = (parentId != null) ? categoryService.getCategoryById(parentId) : null;
        categoryService.createCategory(name, user, parentCategory); // Можно добавить поддержку родительской категории
        return "redirect:/categories"; // Перенаправление на страницу категорий
    }

    // Удаление категории
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        categoryService.deleteCategoryForUser(id, user);
        return "redirect:/categories"; // Перенаправление на страницу категорий
    }
}