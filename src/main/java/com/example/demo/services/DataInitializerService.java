package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для инициализации данных приложения.
 * <p>
 * Этот класс отвечает за создание начальных данных, таких как пользователи,
 * роли, категории, теги и заметки.
 * </p>
 *
 * @author VladimirBoss
 */
@Service
public class DataInitializerService {

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

    @Autowired
    private NoteRepository noteRepository;
    private Logger logger= LoggerFactory.getLogger(DataInitializerService.class);


    /**
     * Инициализирует данные приложения.
     */
    @Transactional
    public void initialize()
    {
        initAll();
    }


    /**
     * Инициализирует все необходимые данные.
     */
    private void initAll() {
        roleInit();
        initUsers();
        initCategories();
        initTags();
        initNotes();
    }

    /**
     * Инициализирует заметки для пользователей.
     */
    private void initNotes() {
        User user1 = userRepository.findByUsername("user").orElseThrow(() -> new IllegalStateException("User not found"));
        var categories = categoryRepository.findAllByUser(user1);
        if (categories.isEmpty()) {
            throw new IllegalStateException("No categories found for the user.");
        }
        var category1 = categories.get(0);
        var category2 = categories.get(categories.size()-1);
        var tags = tagRepository.findAllByUser(user1);
        if (tags.isEmpty()) {
            throw new IllegalStateException("No tags found for the user.");
        }
        var tag1 = tags.get(0);
        var tag2 = tags.get(tags.size()-1);
        Note note1 = new Note();
        note1.setTitle("Title1");
        note1.setText("Text1");
        note1.setPinned(false);
        note1.setUser(user1);
        note1.setCategory(category1);
        note1.addTag(tag1);

        Note note2 = new Note();
        note2.setTitle("Title2");
        note2.setText("Text2");
        note2.setPinned(true);
        note2.setUser(user1);
        note2.setCategory(category2);
        note2.addTag(tag2);

        Note note3 = new Note();
        note3.setTitle("Позвони маме");
        note3.setText("Спроси как она");
        note3.setPinned(false);
        note3.setUser(user1);
        note3.addTag(tag1);
        note3.setCategory(category1);

        noteRepository.save(note1);
        noteRepository.save(note2);
        noteRepository.save(note3);

        logger.info("Standard notes have been initialized.");
    }

    /**
     * Инициализирует пользователей.
     */
    private void initUsers() {
        User user1 = new User("user", "john@example.com", passwordEncoder.encode("123"));
        User user2 = new User("admin", "jane@example.com", passwordEncoder.encode("321"));
        userRepository.save(user1);
        userRepository.save(user2);
        userService.addRole(user2,"ADMIN");
        userService.addRole(user1,"USER");
        logger.info("Users have been initialized.");
    }

    /**
     * Инициализирует категории для пользователей.
     */
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

    /**
     * Инициализирует теги для пользователей.
     */
    private void initTags() {


        User user1 = userRepository.findByUsername("user").orElseThrow(() -> new IllegalStateException("User not found"));
        User user2 = userRepository.findByUsername("admin").orElseThrow(() -> new IllegalStateException("User not found"));

        Tag tag1 = new Tag("Срочное",user1);
        Tag tag2 = new Tag("Личное",user1);
        Tag tag3 = new Tag("Работа",user1);
        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);

        Tag tag1User2 = new Tag("Шпионить",user2);
        Tag tag2User2 = new Tag("Смешное",user2);
        Tag tag3User2 = new Tag("Деньги",user2);
        tagRepository.save(tag1User2);
        tagRepository.save(tag2User2);
        tagRepository.save(tag3User2);
        logger.info("Tags have been initialized.");
    }

    /**
     * Инициализирует роли пользователей.
     */
    private void roleInit() {
        if (roleRepository.findByName("USER").isEmpty()) {
            Role userRole = new Role("USER");
            roleRepository.save(userRole);
            logger.debug("Role USER initialized.");
        }

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = new Role("ADMIN");
            roleRepository.save(adminRole);
            logger.debug("Role ADMIN initialized.");
        }
        if (roleRepository.findByName("PREMIUM").isEmpty()) {
            Role premiumRole = new Role("PREMIUM");
            roleRepository.save(premiumRole);
            logger.debug("Role PREMIUM initialized.");
        }
        logger.info("Roles initialized.");
    }
}
