package com.example.demo.configs;

import com.example.demo.entities.Tag;
import com.example.demo.entities.Category;
import com.example.demo.entities.User;
import com.example.demo.repositories.TagRepository;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Создание тегов
        Tag tag1 = new Tag("Urgent");
        Tag tag2 = new Tag("Personal");
        Tag tag3 = new Tag("Work");

        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);

        // Создание категорий
        Category category1 = new Category("Tasks");
        Category category2 = new Category("Notes");

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        // Создание пользователей
        User user1 = new User("john_doe", "john@example.com", "password123"); // Не забудьте хешировать пароль в реальном приложении!
        User user2 = new User("jane_doe", "jane@example.com", "password123");

        userRepository.save(user1);
        userRepository.save(user2);

        System.out.println("Initial data has been added.");
    }
}