package com.example.demo.repositories;

import com.example.demo.entities.Reminder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностями Reminder.
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций
 * CRUD с напоминаниями.
 * </p>
 *
 * @author VladimirBoss
 */
@Repository
@RepositoryRestResource
public interface ReminderRepository extends CrudRepository<Reminder, Long> {

    /**
     * Находит напоминание по заданному идентификатору.
     *
     * @param id Идентификатор напоминания.
     * @return Напоминание с указанным идентификатором, если оно существует.
     */
    Optional<Reminder> findById(Long id);

    /**
     * Находит все активные напоминания, время которых истекло до заданного момента.
     *
     * @param now Текущий момент времени для сравнения.
     * @return Список активных напоминаний, время которых истекло до указанного момента.
     */
    List<Reminder> findAllByEnabledTrueAndTimeBefore(LocalDateTime now);
}