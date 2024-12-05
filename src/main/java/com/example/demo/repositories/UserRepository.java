package com.example.demo.repositories;

import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностями User.
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций
 * CRUD с пользователями, а также для поиска пользователей по имени
 * и электронной почте.
 * </p>
 *
 * @author VladimirBoss
 */
@Repository
@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Находит пользователя по заданному имени.
     *
     * @param username Имя пользователя для поиска.
     * @return Пользователь с указанным именем, если он существует; иначе пустой Optional.
     */
    Optional<User> findByUsername(String username);

    /**
     * Находит пользователя по заданному адресу электронной почты.
     *
     * @param email Адрес электронной почты для поиска.
     * @return Пользователь с указанным адресом электронной почты, если он существует; иначе пустой Optional.
     */
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findUserByEmail(String email);


    /**
     * Проверяет, существует ли пользователь с заданным именем.
     *
     * @param username Имя пользователя для проверки.
     * @return true, если пользователь существует; иначе false.
     */
    Boolean existsByUsername(String username);


    /**
     * Проверяет, существует ли пользователь с заданным адресом электронной почты.
     *
     * @param email Адрес электронной почты для проверки.
     * @return true, если пользователь существует; иначе false.
     */
    Boolean existsByEmail(String email);

}