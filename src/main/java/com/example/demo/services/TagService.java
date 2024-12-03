package com.example.demo.services;

import com.example.demo.entities.Note;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.repositories.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // Создание нового тега для пользователя
    public Tag createTag(String name, User user) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setUser(user); // Устанавливаем пользователя для тега
        return tagRepository.save(tag);
    }


    // Получение тега по ID
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    // Обновление тега
    public void updateTag(Long id, String name) {
        Tag tag = getTagById(id);
        if (tag != null) {
            tag.setName(name);
            tagRepository.save(tag);
        }
    }


    // Удаление тега по ID
    @Transactional
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    // Получение тегов по пользователю
    public List<Tag> getTagsByUser(User currentUser) {
        return tagRepository.findAllByUser(currentUser);
    }

    // Удаление тега у пользователя
    public void deleteTagForUser(Long tagId, User user) {
        Tag tag = getTagById(tagId);
        if (tag != null && tag.getUser().equals(user)) {
            for (Note note : new ArrayList<>(tag.getNotes())) {
                note.getTags().remove(tag); // Убираем тег из заметки
            }
            tag.setNotes(null);
            deleteTag(tagId); // Удаляем тег, если он принадлежит пользователю
        }
    }

    // Обновление тега у пользователя
    public void updateTagForUser(Long tagId, String newName, User user) {
        Tag tag = getTagById(tagId);
        if (tag != null && tag.getUser().equals(user)) {
            updateTag(tagId, newName); // Обновляем тег, если он принадлежит пользователю
        }
    }
}