package com.example.demo.repositories;

import com.example.demo.entities.Note;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.repositories.criteria.NoteCriteriaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностями Note.
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций
 * CRUD с заметками, а также для поиска заметок по различным критериям.
 * </p>
 *
 * @author VladimirBoss
 */
@Repository
@RepositoryRestResource
public interface NoteRepository extends CrudRepository<Note, Long>
        , NoteCriteriaRepository {

    /**
     * Находит заметки, содержащие заданный заголовок.
     *
     * @param title Заголовок для поиска.
     * @return Список заметок, содержащих указанный заголовок.
     */

    List<Note> findByTitleContaining(String title);


    /**
     * Находит все заметки.
     *
     * @return Список всех заметок.
     */
    List<Note> findAll();


    /**
     * Находит заметки по идентификатору пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Список заметок, принадлежащих пользователю с указанным идентификатором.
     */
    @Query("SELECT n FROM Note n WHERE n.user.id = ?1")
    List<Note> findNotesByUserId(Long userId);


    /**
     * Находит все закрепленные заметки.
     *
     * @return Список всех закрепленных заметок.
     */
    List<Note> findAllByPinnedTrue();

    /**
     * Находит закрепленные заметки пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return Список закрепленных заметок, принадлежащих пользователю с указанным идентификатором.
     */
    List<Note> findNotesByUserIdAndPinnedTrue(Long userId);

    /**
     * Удаляет заметку по заданному идентификатору.
     *
     * @param id Идентификатор заметки, которую нужно удалить.
     */
    @Modifying
    @Query("Delete FROM Note n WHERE n.id =?1")
    void deleteById(Long id);

    /**
     * Находит уникальные заметки по тегам.
     *
     * @param tags Список тегов для поиска заметок.
     * @return Список уникальных заметок, связанных с указанными тегами.
     */
    List<Note> findDistinctByTagsIn(List<Tag> tags);

    /**
     * Ищет заметки пользователя по заданному поисковому термину в заголовке или тексте.
     *
     * @param user       Пользователь, чьи заметки нужно искать.
     * @param searchTerm Поисковый термин для фильтрации заметок.
     * @return Список найденных заметок, соответствующих условиям поиска.
     */
    @Query("SELECT n FROM Note n WHERE n.user = :user AND (LOWER(n.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(n.text) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Note> searchNotesByUserAndTerm(@Param("user") User user, @Param("searchTerm") String searchTerm);
}