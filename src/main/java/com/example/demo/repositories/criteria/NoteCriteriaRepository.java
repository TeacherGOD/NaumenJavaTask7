package com.example.demo.repositories.criteria;

import com.example.demo.entities.Note;

import java.util.List;

public interface NoteCriteriaRepository {
    List<Note> findNotesByTitle(String title);
    List<Note> findNotesByUserId(Long userId);
}