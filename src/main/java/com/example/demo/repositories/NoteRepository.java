package com.example.demo.repositories;

import com.example.demo.entities.Note;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.repositories.criteria.NoteCriteriaRepository;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

@RepositoryRestResource
public interface NoteRepository extends CrudRepository<Note, Long>
        , NoteCriteriaRepository
{

    List<Note> findByTitleContaining(String title);


    List<Note> findAll();
    @Query("SELECT n FROM Note n WHERE n.user.id = ?1")
    List<Note> findNotesByUserId(Long userId);

    List<Note> findAllByPinnedTrue();

    List<Note> findNotesByUserIdAndPinnedTrue(Long userId);


    List<Note> findDistinctByTagsIn(List<Tag> tags);

    @Query("SELECT n FROM Note n WHERE n.user = :user AND (LOWER(n.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(n.text) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Note> searchNotesByUserAndTerm(@Param("user") User user, @Param("searchTerm") String searchTerm);
}