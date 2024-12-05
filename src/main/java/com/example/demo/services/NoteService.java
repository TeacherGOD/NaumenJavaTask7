package com.example.demo.services;

import com.example.demo.entities.Category;
import com.example.demo.entities.Note;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.repositories.NoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для управления заметками.
 * <p>
 * Этот класс предоставляет методы для создания, обновления, получения и удаления заметок,
 * а также для работы с тегами и категориями.
 * </p>
 *
 * @author VladimirBoss
 */
@Service
public class NoteService {

    private final NoteRepository noteRepository;

    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);


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
        note.setPinned(pinned != null && pinned);
        Note savedNote = noteRepository.save(note);
        logger.debug("Created note with ID: {}", savedNote.getId());
        return savedNote;
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
        List<Note> notes = noteRepository.findAll();
        logger.info("Retrieved all notes. Total count: {}", notes.size());
        return notes;
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
            logger.info("Updated note with ID: {}", id);
        }else {
            throw new EntityNotFoundException("Note with id " + id + " not found.");
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
            logger.info("Deleted note with ID: {}", id);
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
            Note savedNote = noteRepository.save(note);
            logger.debug("Pinned note with ID: {}", savedNote.getId());
            return savedNote;
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
            Note savedNote = noteRepository.save(note);
            logger.info("Unpinned note with ID: {}", savedNote.getId());
            return savedNote;
        }
        return null;
    }

    /**
     * Получение всех заметок пользователя по его объекту User.
     *
     * @param currentUser Пользователь, чьи заметки нужно получить.
     * @return Список всех заметок пользователя.
     */
    public List<Note> findNotesByUser(User currentUser) {
        return noteRepository.findNotesByUserId(currentUser.getId());
    }

    /**
     * Получение всех закрепленных заметок пользователя по его объекту User.
     *
     * @param user Пользователь, чьи закрепленные заметки нужно получить.
     * @return Список всех закрепленных заметок пользователя.
     */
    public List<Note> getPinnedNotesByUser(User user) {
        return noteRepository.findNotesByUserIdAndPinnedTrue(user.getId());
    }

    /**
     * Получение всех закрепленных заметок в системе.
     *
     * @return Список всех закрепленных заметок.
     */
    public List<Note> findAllPinned() {
        return noteRepository.findAllByPinnedTrue();
    }

    /**
     * Получение всех закрепленных заметок пользователя по его объекту User.
     *
     * @param currentUser Пользователь, чьи закрепленные заметки нужно получить.
     * @return Список всех закрепленных заметок пользователя.
     */
    public List<Note> findPinnedByUser(User currentUser) {
        return noteRepository.findNotesByUserIdAndPinnedTrue(currentUser.getId());
    }

    /**
     * Поиск заметки по ID с выбросом исключения при отсутствии найденной записи.
     *
     * @param id ID искомой заметки.
     * @return Найденная заметка.
     * @throws IllegalArgumentException Если запись не найдена.
     */
    public Note findNote(Long id) {
        return noteRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Note with id "+ id+"is not found.") );
    }

    /**
     * Переключение статуса закрепления у указанной заметки.
     *
     * @param id ID искомой заметки.
     */
    public void togglePin(Long id){
        Note note = findNote(id);
        note.setPinned(!note.isPinned());
        noteRepository.save(note);

    }

    /**
     * Метод для получения замечаний по тегам.
     *
     * @param tags Список тегов.
     * @return Список найденных замечаний.
     */
    public List<Note> getNotesByTags(List<Tag> tags) {
        return noteRepository.findDistinctByTagsIn(tags);
    }

    /**
     * Поиск замечаний пользователя по заданному поисковому термину.
     *
     * @param user Пользователь, чьи замечания нужно искать.
     * @param searchTerm Поисковый термин.
     * @return Список найденных замечаний.
     */
    public List<Note> searchNotesByUserAndTerm(User user, String searchTerm) {
        return noteRepository.searchNotesByUserAndTerm(user, searchTerm);
    }
}