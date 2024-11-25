package com.example.demo.services;

import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.repositories.TagRepository;
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

    public Tag createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return tagRepository.save(tag);
    }

    public List<Tag> getAllTags() {
        // Преобразуем Iterable<Tag> в List<Tag>
        return StreamSupport.stream(tagRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    public void updateTag(Long id, String name) {
        Tag tag = getTagById(id);
        if (tag != null) {
            tag.setName(name);
            tagRepository.save(tag);
        }
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }


    public List<Tag> getTagsByUser(User currentUser) {
        return tagRepository.findAllByUser(currentUser);
    }
}