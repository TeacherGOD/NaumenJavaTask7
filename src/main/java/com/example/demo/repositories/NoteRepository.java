package com.example.demo.repositories;

import com.example.demo.entities.Note;
import com.example.demo.repositories.criteria.NoteCriteriaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

@RepositoryRestResource
public interface NoteRepository extends CrudRepository<Note, Long>, NoteCriteriaRepository {

    List<Note> findByTitleContaining(String title);

    @Query("SELECT n FROM Note n WHERE n.user.id = ?1")
    List<Note> findNotesByUserId(Long userId);


}