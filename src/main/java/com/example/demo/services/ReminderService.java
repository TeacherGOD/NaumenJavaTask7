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

/**
 * Сервис для управления напоминаниями.
 * <p>
 * Этот класс предоставляет методы для создания, обновления, удаления и проверки напоминаний,
 * а также их связи с заметками.
 * </p>
 *
 * @author VladimirBoss
 */
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

    /**
     * Создание заметки с напоминанием.
     *
     * @param note     Заметка, к которой будет привязано напоминание.
     * @param reminder Напоминание, которое будет связано с заметкой.
     */
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
        logger.info("Created note with ID: {} and associated reminder.", savedNote.getId());

    }

    /**
     * Добавление напоминания к существующей заметке.
     *
     * @param noteId   ID заметки, к которой будет добавлено напоминание.
     * @param reminder  Напоминание для добавления.
     * @return Добавленное напоминание.
     */
    @Transactional
    public Reminder addReminderToNote(Long noteId, Reminder reminder) {
        Optional<Note> optionalNote = noteRepository.findById(noteId);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            reminder.setNote(note);
            note.setReminder(reminder);
            var savedReminder =reminderRepository.save(reminder);
            noteRepository.save(note);
            logger.info("Added reminder to note with ID: {}", noteId);
            return savedReminder;
        } else {
            throw new IllegalArgumentException("Note with id " + noteId + " not found");
        }
    }

    /**
     * Удаление напоминания по ID.
     *
     * @param reminderId ID напоминания для удаления.
     */
    @Transactional
    public void removeReminder(Long reminderId) {
        Optional<Reminder> optionalReminder = reminderRepository.findById(reminderId);
        if (optionalReminder.isPresent()) {
            var reminder = optionalReminder.get();
            var note = reminder.getNote();
            note.setReminder(null);
            reminderRepository.delete(reminder);
            noteRepository.save(note);
            logger.info("Removed reminder with ID: {}", reminderId);
        } else {
            throw new IllegalArgumentException("Reminder with id " + reminderId + " not found");
        }
    }

    /**
     * Получение напоминания по ID.
     *
     * @param reminderId ID напоминания.
     * @return Найденное напоминание.
     */
    public Reminder getReminderById(Long reminderId) {
        Optional<Reminder> optionalReminder = reminderRepository.findById(reminderId);
        if (optionalReminder.isPresent()) {
            return optionalReminder.get();
        }
        throw new IllegalArgumentException("Reminder with id " + reminderId + " not found");
    }

    /**
     * Проверка и отправка уведомлений по истекшим напоминаниям.
     */
    @Scheduled(fixedRate = 60*1000/10) //60*1000=минута
    public void checkReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Reminder> reminders = reminderRepository.findAllByEnabledTrueAndTimeBefore(now);
        for (Reminder reminder : reminders) {
            notificationController.sendReminder(reminder);
            reminder.setEnabled(false);
            reminderRepository.save(reminder);
            logger.info("Sent notification for reminder: {}", reminder.getId());
        }
    }

}