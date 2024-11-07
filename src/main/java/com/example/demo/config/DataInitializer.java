package com.example.demo.config;

import com.example.demo.entities.Role;
import com.example.demo.entities.Tag;
import com.example.demo.entities.Category;
import com.example.demo.entities.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.TagRepository;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired UserService userService;

    @Override
    public void run(String... args) throws Exception {
        initAll();





        System.out.println("Initial data has been added.");
    }

    private void initAll() {
        initTags();

        initCategories();

        roleInit();
        initUsers();



    }

    private void initUsers() {

        User user1 = new User("user", "john@example.com", passwordEncoder.encode("123"));
        User user2 = new User("admin", "jane@example.com", passwordEncoder.encode("321"));


        userRepository.save(user1);
        userRepository.save(user2);
        userService.addRole(user2,"ADMIN");

    }

    private void initCategories() {

        Category category1 = new Category("Tasks");
        Category category2 = new Category("Notes");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
    }

    private void initTags() {

        Tag tag1 = new Tag("Urgent");
        Tag tag2 = new Tag("Personal");
        Tag tag3 = new Tag("Work");

        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);
    }

    private void roleInit() {
        if (roleRepository.findByName("USER").isEmpty()) {
            Role userRole = new Role("USER");
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = new Role("ADMIN");
            roleRepository.save(adminRole);
        }

        System.out.println("Roles initialized.");
    }
}