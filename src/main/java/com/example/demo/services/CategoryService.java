package com.example.demo.services;

import com.example.demo.entities.Category;
import com.example.demo.entities.User;
import com.example.demo.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private NoteService noteService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Создание новой категории для пользователя
    public Category createCategory(String name, User user, Category parentCategory) {
        Category category = new Category();
        category.setName(name);
        category.setUser(user); // Устанавливаем пользователя для категории
        if (parentCategory!= null) {
            category.setParent(parentCategory);
        }
        return categoryRepository.save(category);
    }


    // Получение категории по ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // Обновление категории
    public void updateCategory(Long id, String name) {
        Category category = getCategoryById(id);
        if (category != null) {
            category.setName(name);
            categoryRepository.save(category);
        }
    }

    // Удаление категории по ID
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // Получение категорий по пользователю
    public List<Category> getCategoriesByUser(User user) {
        return categoryRepository.findAllByUser(user);
    }

    // Удаление категории у пользователя
    @Transactional
    public void deleteCategoryForUser(Long categoryId, User user) {
        Category category = getCategoryById(categoryId);
        if (category != null && category.getUser().equals(user)) {
//            for (Category childrenCategory : category.getChildren())
//            {
//                deleteCategoryForUser(childrenCategory.getId(), user); // Рекурсивно удаляем дочерние категории
//            }
//            category.getNotes().forEach(note -> noteService.deleteNote(note.getId())
//            );
            deleteCategory(categoryId); // Удаляем категорию, если она принадлежит пользователю
        }
    }

    // Обновление категории у пользователя
    public void updateCategoryForUser(Long categoryId, String newName, User user) {
        Category category = getCategoryById(categoryId);
        if (category != null && category.getUser().equals(user)) {
            updateCategory(categoryId, newName); // Обновляем категорию, если она принадлежит пользователю
        }
    }
}