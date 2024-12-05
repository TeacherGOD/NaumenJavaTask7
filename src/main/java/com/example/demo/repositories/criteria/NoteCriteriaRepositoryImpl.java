package com.example.demo.repositories.criteria;

import com.example.demo.entities.Note;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Реализация интерфейса NoteCriteriaRepository для выполнения запросов к заметкам.
 * <p>
 * Этот класс использует JPA Criteria API для построения запросов
 * к базе данных на основе различных критериев.
 * </p>
 *
 * @author VladimirBoss
 */
@Repository
public class NoteCriteriaRepositoryImpl implements NoteCriteriaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Находит список заметок по заданному заголовку.
     *
     * @param title Заголовок заметки для поиска.
     * @return Список заметок, соответствующих заданному заголовку.
     */
    @Override
    public List<Note> findNotesByTitle(String title) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Note> query = cb.createQuery(Note.class);
        Root<Note> note = query.from(Note.class);

        query.select(note).where(cb.like(note.get("title"), "%" + title + "%"));

        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Находит список заметок, принадлежащих пользователю с заданным идентификатором.
     *
     * @param userId Идентификатор пользователя для поиска его заметок.
     * @return Список заметок, принадлежащих пользователю с заданным идентификатором.
     */
    @Override
    public List<Note> findNotesByUserId(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Note> query = cb.createQuery(Note.class);
        Root<Note> note = query.from(Note.class);

        query.select(note).where(cb.equal(note.get("user").get("id"), userId));

        return entityManager.createQuery(query).getResultList();
    }
}