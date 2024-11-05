package com.example.demo.repositories;

import com.example.demo.entities.Reminder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository

@RepositoryRestResource
public interface ReminderRepository extends CrudRepository<Reminder, Long> {


    List<Reminder> findByReminderTimeBetween(Date startTime, Date endTime);

    Optional<Reminder> findById(Long id);
}