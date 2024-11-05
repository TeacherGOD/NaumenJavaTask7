package com.example.demo.tests;

import com.example.demo.entities.Note;
import com.example.demo.entities.User;
import com.example.demo.repositories.NoteRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByTitleContaining() {
        // Создание пользователя
        User user = new User();
        user.setUsername("testUser");
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
}