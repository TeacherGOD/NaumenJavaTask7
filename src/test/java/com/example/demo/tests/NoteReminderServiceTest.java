package com.example.demo.tests;

import com.example.demo.entities.Note;
import com.example.demo.entities.Reminder;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.repositories.NoteRepository;
import com.example.demo.repositories.ReminderRepository;
import com.example.demo.repositories.TagRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.NoteReminderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class NoteReminderServiceTest {

    @Autowired
    private NoteReminderService noteReminderService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void testCreateNoteWithReminder() {

        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);

        Tag tag1 = new Tag();
        tag1.setName("Default");
        tagRepository.save(tag1);
        Tag tag = tagRepository.findByName("Default").orElseGet(() -> {
            Tag newTag = new Tag();
            newTag.setName("Default");
            return tagRepository.save(newTag); // Сохраняем новый тег, если он не найден
        });


        Note note = new Note();
        note.setTitle("Buy groceries");
        note.setText("Don't forget to buy milk and eggs.");
        note.setUser(user);
        note.addTag(tag);

        Reminder reminder = new Reminder();
        reminder.setReminderTime(new java.util.Date());
        reminder.setNote(note);

        noteReminderService.createNoteWithReminder(note, reminder);

        Assertions.assertNotNull(note.getId());
        Assertions.assertNotNull(reminder.getId());

        Reminder foundReminder = reminderRepository.findById(reminder.getId()).orElse(null);

        Assertions.assertNotNull(foundReminder);
        Assertions.assertEquals(note.getId(), foundReminder.getNote().getId());
    }

    @Test
    void testCreateNoteWithInvalidReminder() {
        // Попытка создать напоминание без заметки (отрицательный кейс)
        Reminder reminder = new Reminder();
        reminder.setReminderTime(new java.util.Date());

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            noteReminderService.createNoteWithReminder(null, reminder);
        });

        String expectedMessage = "Note must not be null";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}