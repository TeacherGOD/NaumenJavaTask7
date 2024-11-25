package com.example.demo.services;

import com.example.demo.entities.Category;
import com.example.demo.entities.User;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void updateCategory(Long id, String name) {
        Category category = getCategoryById(id);
        if (category != null) {
            category.setName(name);
            categoryRepository.save(category);
        }
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }


    public List<Category> getCategoriesByUser(User user) {
        return categoryRepository.findAllByUser(user);
    }
}