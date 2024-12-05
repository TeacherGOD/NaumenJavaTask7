package com.example.demo.repositories;

import com.example.demo.entities.Category;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностями Category.
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций
 * CRUD с категориями, а также для поиска категорий по имени и пользователю.
 * </p>
 *
 * @author VladimirBoss
 */
@Repository
@RepositoryRestResource
public interface CategoryRepository extends CrudRepository<Category, Long> {

    /**
     * Находит категорию по имени и пользователю.
     *
     * @param name Имя категории.
     * @param user Пользователь, которому принадлежит категория.
     * @return Найденная категория или null, если категория не найдена.
     */
    Category findByNameAndUser(String name, User user);

    /**
     * Находит все категории, принадлежащие заданному пользователю.
     *
     * @param user Пользователь, для которого нужно найти категории.
     * @return Список категорий, принадлежащих пользователю.
     */
    List<Category> findAllByUser(User user);


    /**
     * Удаляет категорию по заданному идентификатору.
     *
     * @param id Идентификатор категории, которую нужно удалить.
     */
    @Modifying
    @Query("Delete FROM Category c WHERE c.id=?1 ")
    void deleteById(Long id);
}