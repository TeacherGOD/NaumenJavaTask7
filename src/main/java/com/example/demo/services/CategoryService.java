package com.example.demo.services;

import com.example.demo.entities.Category;
import com.example.demo.entities.User;
import com.example.demo.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Сервис для управления категориями.
 * <p>
 * Этот класс предоставляет методы для создания, обновления, получения и удаления категорий,
 * связанных с пользователями.
 * </p>
 *
 * @author VladimirBoss
 */
@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private NoteService noteService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Создает новую категорию для указанного пользователя.
     *
     * @param name Название категории.
     * @param user Пользователь, которому принадлежит категория.
     * @param parentCategory Родительская категория (может быть null).
     * @return Созданная категория.
     */
    public Category createCategory(String name, User user, Category parentCategory) {
        Category category = new Category();
        category.setName(name);
        category.setUser(user); // Устанавливаем пользователя для категории
        if (parentCategory!= null) {
            category.setParent(parentCategory);
        }
        logger.debug("Создана новая категория: {} для пользователя: {}", name, user.getUsername());
        return categoryRepository.save(category);
    }


    /**
     * Получает категорию по заданному идентификатору.
     *
     * @param id Идентификатор категории.
     * @return Найденная категория или null, если категория не найдена.
     */
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }


    /**
     * Обновляет название категории по заданному идентификатору.
     *
     * @param id Идентификатор категории.
     * @param name Новое название категории.
     */
    public void updateCategory(Long id, String name) {
        Category category = getCategoryById(id);
        if (category != null) {
            category.setName(name);
            categoryRepository.save(category);
            logger.info("Обновлено название категории с ID: {} на {}", id, name);
        }else {
            logger.warn("Попытка обновления несуществующей категории с ID: {}", id);
        }
    }

    /**
     * Получает все категории, принадлежащие указанному пользователю.
     *
     * @param user Пользователь, для которого нужно получить категории.
     * @return Список категорий, принадлежащих пользователю.
     */
    public List<Category> getCategoriesByUser(User user) {
        return categoryRepository.findAllByUser(user);
    }

    /**
     * Удаляет категорию у указанного пользователя.
     * Рекурсивно удаляет дочерние категории и связанные заметки.
     *
     * @param categoryId Идентификатор категории для удаления.
     * @param user Пользователь, которому принадлежит категория.
     */
    @Transactional
    public void deleteCategoryForUser(Long categoryId, User user) {
        Category category = getCategoryById(categoryId);
        if (category != null && category.getUser().equals(user)) {
            for (Category childrenCategory : category.getChildren())
            {
                deleteCategoryForUser(childrenCategory.getId(), user); // Рекурсивно удаляем дочерние категории,
                //! почему каскадирование не работает?
            }
            category.getNotes().forEach(
                    note -> noteService.deleteNote(note.getId())
            );  //! почему каскадирование не работает?
            categoryRepository.deleteById(categoryId);
            logger.info("Категория с ID: {} была удалена для пользователя: {}", categoryId, user.getUsername());
        }else {
            logger.warn("Попытка удаления категории с ID: {} не удалась. Категория не найдена или не принадлежит пользователю.", categoryId);
        }

    }

    /**
     * Обновляет название категории у указанного пользователя.
     *
     * @param categoryId Идентификатор категории для обновления.
     * @param newName Новое название категории.
     * @param user Пользователь, которому принадлежит категория.
     */
    public void updateCategoryForUser(Long categoryId, String newName, User user) {
        Category category = getCategoryById(categoryId);
        if (category != null && category.getUser().equals(user)) {
            updateCategory(categoryId, newName);
            logger.info("Категория с ID: {} была обновлена на новое имя: {}", categoryId, newName);
        }else {
            logger.warn("Попытка обновления категории с ID: {} не удалась. Категория не найдена или не принадлежит пользователю.", categoryId);
        }

    }
}