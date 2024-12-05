package com.example.demo.repositories;

import com.example.demo.entities.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностями Role.
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций
 * CRUD с ролями, а также для поиска ролей по имени.
 * </p>
 *
 * @author VladimirBoss
 */
@Repository
@RepositoryRestResource
public interface RoleRepository extends CrudRepository<Role, Long> {

    /**
     * Находит роль по заданному имени.
     *
     * @param name Имя роли для поиска.
     * @return Роль с указанным именем, если она существует; иначе пустой Optional.
     */
    Optional<Role> findByName(String name);
}