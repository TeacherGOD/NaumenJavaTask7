package com.example.demo.services;

import com.example.demo.entities.Note;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.repositories.TagRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Сервис для управления тегами.
 * <p>
 * Этот класс предоставляет методы для создания, обновления,
 * получения и удаления тегов, связанных с пользователями.
 * </p>
 *
 * @author VladimirBoss
 */
@Service
public class TagService {
    private final TagRepository tagRepository;

    private static final Logger logger = LoggerFactory.getLogger(TagService.class);


    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    /**
     * Создание нового тега для пользователя.
     *
     * @param name Название тега.
     * @param user Пользователь, которому принадлежит тег.
     * @return Созданный тег.
     */
    public Tag createTag(String name, User user) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setUser(user);Tag savedTag = tagRepository.save(tag);
        logger.debug("Created tag with ID: {} for user: {}", savedTag.getId(), user.getUsername());
        return savedTag;
    }

    /**
     * Получение тега по ID.
     *
     * @param id ID тега.
     * @return Найденный тег или null, если не найден.
     */
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    /**
     * Обновление тега.
     *
     * @param id   ID тега для обновления.
     * @param name Новое название тега.
     */
    public void updateTag(Long id, String name) {
        Tag tag = getTagById(id);
        if (tag != null) {
            tag.setName(name);
            tagRepository.save(tag);
            logger.info("Updated tag with ID: {}", id);
        } else {
            logger.warn("Attempted to update non-existent tag with ID: {}", id);
        }
    }

    /**
     * Удаление тега по ID.
     *
     * @param id ID тега для удаления.
     */
    @Transactional
    public void deleteTag(Long id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
            logger.info("Deleted tag with ID: {}", id);
        } else {
            logger.warn("Attempted to delete non-existent tag with ID: {}", id);
        }

    }

    /**
     * Получение тегов по пользователю.
     *
     * @param currentUser Пользователь, чьи теги нужно получить.
     * @return Список тегов, принадлежащих пользователю.
     */
    public List<Tag> getTagsByUser(User currentUser) {
        return tagRepository.findAllByUser(currentUser);
    }

    /**
     * Удаление тега у пользователя.
     *
     * @param tagId ID тега для удаления.
     * @param user   Пользователь, которому принадлежит тег.
     */
    public void deleteTagForUser(Long tagId, User user) {
        Tag tag = getTagById(tagId);
        if (tag != null && tag.getUser().equals(user)) {
            for (Note note : new ArrayList<>(tag.getNotes())) {
                note.getTags().remove(tag);
            }
            tag.setNotes(null);
            deleteTag(tagId);
            logger.info("Deleted tag with ID: {} for user: {}", tagId, user.getUsername());
        } else {
            logger.warn("Attempted to delete a non-existent or unauthorized tag with ID: {} for user: {}", tagId, user.getUsername());
        }
    }

    /**
     * Обновление тега у пользователя.
     *
     * @param tagId   ID тега для обновления.
     * @param newName Новое название тега.
     * @param user    Пользователь, которому принадлежит тег.
     */
    public void updateTagForUser(Long tagId, String newName, User user) {
        Tag tag = getTagById(tagId);
        if (tag != null && tag.getUser().equals(user)) {
            updateTag(tagId, newName);
            logger.info("Updated tag with ID: {} for user: {}", tagId, user.getUsername());
        } else {
            logger.warn("Attempted to update a non-existent or unauthorized tag with ID: {} for user: {}", tagId, user.getUsername());
        }
    }
}