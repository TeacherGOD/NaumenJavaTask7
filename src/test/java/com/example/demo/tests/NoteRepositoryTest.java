package com.example.demo.tests;

import com.example.demo.controllers.NoteController;
import com.example.demo.entities.Note;
import com.example.demo.entities.User;
import com.example.demo.repositories.NoteRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.CategoryService;
import com.example.demo.services.NoteService;
import com.example.demo.services.TagService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@SpringBootTest
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteService noteService;

    @Autowired
    private TagService tagService;

    @Autowired
    private NoteController noteController;
    @Autowired
    private CategoryService categoryService;

    @Test
    void testFindByTitleContaining() {
        // Создание пользователя
        User user = new User();
        user.setUsername("testUser2");
        userRepository.save(user);

        // Создание заметки
        Note note = new Note();
        note.setTitle("Important Note");
        note.setText("This is a test note.");
        note.setUser(user);

        Note savedNote = noteRepository.save(note);

        List<Note> foundNotes = noteRepository.findByTitleContaining("Important");

        Assertions.assertFalse(foundNotes.isEmpty());

        Note foundNote = foundNotes.stream()
                .filter(n -> n.getId().equals(savedNote.getId()))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(foundNote);
        Assertions.assertEquals(savedNote.getId(), foundNote.getId());
        Assertions.assertEquals(savedNote.getTitle(), foundNote.getTitle());
        Assertions.assertEquals(savedNote.getText(), foundNote.getText());
    }

    @Test
    @Transactional
    void testDelete() {
        // Создание пользователя
        User user = new User();
        user.setUsername("testUser3");
        userRepository.save(user);


        var tag=tagService.getTagById(2L);

        var category=categoryService.getCategoryById(1L);


        // Создание заметки
        Note note = new Note();
        note.setTitle("Important Note");
        note.setText("This is a test note.");
        note.setUser(user);

        note.addTag(tag);
        note.setCategory(category);


        Note savedNote = noteRepository.save(note);

//        noteService.deleteNote(note.getId());

        noteController.deleteNote(note.getId(), user);

        Note deletedNote = noteRepository.findById(savedNote.getId()).orElse(null);

        Assertions.assertNull(deletedNote);
    }
}