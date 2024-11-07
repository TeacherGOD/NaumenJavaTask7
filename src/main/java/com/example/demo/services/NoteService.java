package com.example.demo.services;

import com.example.demo.entities.Note;
import com.example.demo.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Создание новой заметки.
     *
     * @param title  Заголовок заметки.
     * @param text   Текст заметки.
     * @param pinned Статус закрепления заметки.
     * @return Созданная заметка.
     */
    public Note createNote(String title, String text, Boolean pinned) {
        Note note = new Note();
        note.setTitle(title);
        note.setText(text);
        note.setPinned(pinned != null && pinned); // Устанавливаем статус pinned, если он не null
        return noteRepository.save(note);
    }


    public Note createNote(Note note)
    {
        return noteRepository.save(note);
    }

    /**
     * Получение всех заметок.
     *
     * @return Список всех заметок.
     */
    public List<Note> getAllNotes() {
        return StreamSupport.stream(noteRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Получение заметки по ID.
     *
     * @param id ID заметки.
     * @return Найденная заметка или null, если не найдена.
     */
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    /**
     * Обновление существующей заметки.
     *
     * @param id    ID заметки для обновления.
     * @param title Новый заголовок заметки.
     * @param text  Новый текст заметки.
     */
    public void updateNote(Long id, String title, String text) {
        Note note = getNoteById(id);
        if (note != null) {
            note.setTitle(title);
            note.setText(text);
            noteRepository.save(note);
        }
    }

    /**
     * Удаление заметки по ID.
     *
     * @param id ID заметки для удаления.
     */
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    /**
     * Закрепление заметки по ID.
     *
     * @param id ID заметки для закрепления.
     * @return Закрепленная заметка.
     */
    public Note pinNote(Long id) {
        Note note = getNoteById(id);
        if (note != null) {
            note.setPinned(true);
            return noteRepository.save(note);
        }
        return null;
    }

    /**
     * Открепление заметки по ID.
     *
     * @param id ID заметки для открепления.
     * @return Открепленная заметка.
     */
    public Note unpinNote(Long id) {
        Note note = getNoteById(id);
        if (note != null) {
            note.setPinned(false);
            return noteRepository.save(note);
        }
        return null;
    }
}