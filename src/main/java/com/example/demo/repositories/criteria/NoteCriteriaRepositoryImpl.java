package com.example.demo.repositories.criteria;

import com.example.demo.entities.Note;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteCriteriaRepositoryImpl implements NoteCriteriaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Note> findNotesByTitle(String title) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Note> query = cb.createQuery(Note.class);
        Root<Note> note = query.from(Note.class);

        query.select(note).where(cb.like(note.get("title"), "%" + title + "%"));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Note> findNotesByUserId(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Note> query = cb.createQuery(Note.class);
        Root<Note> note = query.from(Note.class);

        query.select(note).where(cb.equal(note.get("user").get("id"), userId));

        return entityManager.createQuery(query).getResultList();
    }
}