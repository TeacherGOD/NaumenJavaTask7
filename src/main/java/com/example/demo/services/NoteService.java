package com.example.demo.services;

import com.example.demo.entities.Category;
import com.example.demo.entities.Note;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.repositories.NoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return noteRepository.findAll();
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
     * @param id       ID заметки для обновления.
     * @param title    Новый заголовок заметки.
     * @param text     Новый текст заметки.
     * @param category
     * @param tags
     */
    public void updateNote(Long id, String title, String text, Category category, List<Tag> tags) {
        Note note = getNoteById(id);
        if (note != null) {
            note.setTitle(title);
            note.setText(text);
            note.setCategory(category);
            note.setTags(tags);
            noteRepository.save(note);
        }
    }

    /**
     * Удаление заметки по ID.
     *
     * @param id ID заметки для удаления.
     */
    @Transactional
    public void deleteNote(Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Note with id " + id + " not found");
        }

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

    public List<Note> findNotesByUser(User currentUser) {
        return noteRepository.findNotesByUserId(currentUser.getId());
    }

    public List<Note> getPinnedNotesByUser(User user) {
        return noteRepository.findNotesByUserIdAndPinnedTrue(user.getId());
    }

    public List<Note> findAllPinned() {
        return noteRepository.findAllByPinnedTrue();
    }

    public List<Note> findPinnedByUser(User currentUser) {
        return noteRepository.findNotesByUserIdAndPinnedTrue(currentUser.getId());
    }

    public Note findNote(Long id) {
        return noteRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Note with id "+ id+"is not found.") );
    }

    public void togglePin(Long id){
        Note note = findNote(id);
        note.setPinned(!note.isPinned());
        noteRepository.save(note);

    }

    // Метод для получения заметок по тегам
    public List<Note> getNotesByTags(List<Tag> tags) {
        return noteRepository.findDistinctByTagsIn(tags);
    }


    public List<Note> searchNotesByUserAndTerm(User user, String searchTerm) {
        return noteRepository.searchNotesByUserAndTerm(user, searchTerm);
    }
}