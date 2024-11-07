package com.example.demo.services;

import com.example.demo.entities.Note;
import com.example.demo.entities.Reminder;

import com.example.demo.entities.Tag;
import com.example.demo.repositories.NoteRepository;
import com.example.demo.repositories.ReminderRepository;
import com.example.demo.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteReminderService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private TagRepository tagRepository;

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
}