package com.example.demo.services;

import com.example.demo.controllers.NotificationController;
import com.example.demo.entities.Note;
import com.example.demo.entities.Reminder;
import com.example.demo.entities.Tag;
import com.example.demo.repositories.NoteRepository;
import com.example.demo.repositories.ReminderRepository;
import com.example.demo.repositories.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReminderService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private NotificationController notificationController;


    private Logger logger= LoggerFactory.getLogger(ReminderService.class);

    @Transactional
    public void createNoteWithReminder(Note note, Reminder reminder) {
        if (note == null) {
            throw new IllegalArgumentException("Note must not be null");
        }


        List<Tag> updatedTags = new ArrayList<>();
        for (Tag tag : note.getTags()) {

            Tag existingTag = tagRepository.findByName(tag.getName()).orElseGet(() -> {

                return tagRepository.save(tag);
            });
            updatedTags.add(existingTag);
        }

        note.setTags(updatedTags);



        Note savedNote = noteRepository.save(note);

        reminder.setNote(savedNote);

        reminderRepository.save(reminder);
    }

    @Transactional
    public Reminder addReminderToNote(Long noteId, Reminder reminder) {
        Optional<Note> optionalNote = noteRepository.findById(noteId);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            reminder.setNote(note);
            note.setReminder(reminder);
            var reminder1=reminderRepository.save(reminder);
            noteRepository.save(note);
            return reminder1;
        } else {
            throw new IllegalArgumentException("Note with id " + noteId + " not found");
        }
    }

    @Transactional
    public void removeReminder(Long reminderId) {
        Optional<Reminder> optionalReminder = reminderRepository.findById(reminderId);
        if (optionalReminder.isPresent()) {
            var reminder = optionalReminder.get();
            var note = reminder.getNote();
            note.setReminder(null);
            reminderRepository.delete(reminder);
            noteRepository.save(note);
        } else {
            throw new IllegalArgumentException("Reminder with id " + reminderId + " not found");
        }
    }

    public Reminder getReminderById(Long reminderId) {
        Optional<Reminder> optionalReminder = reminderRepository.findById(reminderId);
        if (optionalReminder.isPresent()) {
            return optionalReminder.get();
        }
        throw new IllegalArgumentException("Reminder with id " + reminderId + " not found");
    }

    @Scheduled(fixedRate = 60*1000/10) //60*1000=минута
    public void checkReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Reminder> reminders = reminderRepository.findAllByEnabledTrueAndTimeBefore(now);
        for (Reminder reminder : reminders) {
            sendNotification(reminder);
            notificationController.sendReminder(reminder);
            reminder.setEnabled(false);
            reminderRepository.save(reminder);
        }
    }

    private void sendNotification(Reminder reminder) {
        logger.info("Reminder: '" + reminder.getNote().getTitle() + "' Прошло. "+reminder.getNote().getUser().getUsername()+" Должен быть уведомлен!");
    }
}