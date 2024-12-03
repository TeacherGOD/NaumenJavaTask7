package com.example.demo.tests;

import com.example.demo.entities.Note;
import com.example.demo.entities.User;
import com.example.demo.repositories.NoteRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.criteria.NoteCriteriaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class NoteCriteriaRepositoryTest {


    @Qualifier("noteCriteriaRepositoryImpl")
    @Autowired
    private NoteCriteriaRepository noteCriteriaRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindNotesByTitle() {


        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword");
        userRepository.save(user);

        Note note = new Note();
        note.setTitle("Important Task");
        note.setText("This task is important.");
        note.setUser(user);

        Note savedNote = noteRepository.save(note);

        List<Note> foundNotes = noteCriteriaRepository.findNotesByTitle("Important");

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
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}