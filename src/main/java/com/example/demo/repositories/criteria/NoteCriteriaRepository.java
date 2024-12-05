package com.example.demo.repositories.criteria;

import com.example.demo.entities.Note;

import java.util.List;

/**
 * Интерфейс для определения критериев поиска заметок.
 * <p>
 * Этот интерфейс предоставляет методы для выполнения запросов
 * к заметкам на основе различных критериев, таких как заголовок
 * и идентификатор пользователя.
 * </p>
 *
 * @author VladimirBoss
 */
public interface NoteCriteriaRepository {
    /**
     * Находит список заметок по заданному заголовку.
     *
     * @param title Заголовок заметки для поиска.
     * @return Список заметок, соответствующих заданному заголовку.
     */
    List<Note> findNotesByTitle(String title);

    /**
     * Находит список заметок, принадлежащих пользователю с заданным идентификатором.
     *
     * @param userId Идентификатор пользователя для поиска его заметок.
     * @return Список заметок, принадлежащих пользователю с заданным идентификатором.
     */
    List<Note> findNotesByUserId(Long userId);
}