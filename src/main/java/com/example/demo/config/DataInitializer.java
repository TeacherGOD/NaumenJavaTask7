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
        roleInit();
        initUsers();

        initCategories();
        initTags();



    }

    private void initUsers() {

        User user1 = new User("user", "john@example.com", passwordEncoder.encode("123"));
        User user2 = new User("admin", "jane@example.com", passwordEncoder.encode("321"));


        userRepository.save(user1);
        userRepository.save(user2);
        userService.addRole(user2,"ADMIN");

    }

    private void initCategories() {

        User user1 = userRepository.findByUsername("user").orElseThrow(() -> new IllegalStateException("User not found"));
        User user2 = userRepository.findByUsername("admin").orElseThrow(() -> new IllegalStateException("User not found"));



        Category category1 = new Category("Tasks",user1);
        Category category2 = new Category("Notes",user1);
        Category category3 = new Category("Private",category2,user1);
        Category category4 = new Category("SUPER PRIVATE",category3,user1);
        Category category5 = new Category("Public",category2,user1);

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);
        categoryRepository.save(category5);

        Category category1User2=new Category("Tasks",user2);
        Category category2User2=new Category("Notes",user2);
        Category category3User2=new Category("Admin thoughts",category2User2,user2);

        categoryRepository.save(category1User2);
        categoryRepository.save(category2User2);
        categoryRepository.save(category3User2);


    }

    private void initTags() {


        User user1 = userRepository.findByUsername("user").orElseThrow(() -> new IllegalStateException("User not found"));
        User user2 = userRepository.findByUsername("admin").orElseThrow(() -> new IllegalStateException("User not found"));

        Tag tag1 = new Tag("Urgent",user1);
        Tag tag2 = new Tag("Personal",user1);
        Tag tag3 = new Tag("Work",user1);
        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);

        Tag tag1User2 = new Tag("Spy",user2);
        Tag tag2User2 = new Tag("Hi-hi",user2);
        Tag tag3User2 = new Tag("Money",user2);
        tagRepository.save(tag1User2);
        tagRepository.save(tag2User2);
        tagRepository.save(tag3User2);
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