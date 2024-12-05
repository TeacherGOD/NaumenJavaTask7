package com.example.demo.repositories;

import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностями Tag.
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций
 * CRUD с тегами, а также для поиска тегов по имени и пользователю.
 * </p>
 *
 * @author VladimirBoss
 */
@Repository
@RepositoryRestResource
public interface TagRepository extends CrudRepository<Tag, Long> {

    /**
     * Находит тег по заданному имени.
     *
     * @param name Имя тега для поиска.
     * @return Тег с указанным именем, если он существует; иначе пустой Optional.
     */
    Optional<Tag> findByName(String name);

    /**
     * Удаляет тег по заданному идентификатору.
     *
     * @param id Идентификатор тега, который нужно удалить.
     */
    @Modifying
    @Query("Delete FROM Tag t WHERE t.id=?1 ")
    void deleteById(Long id);

    /**
     * Находит все теги, принадлежащие заданному пользователю.
     *
     * @param currentUser Пользователь, для которого нужно найти теги.
     * @return Список тегов, принадлежащих пользователю.
     */
    List<Tag> findAllByUser(User currentUser);

}
