package com.example.demo.repositories;

import com.example.demo.entities.Reminder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends CrudRepository<Reminder, Long> {


    List<Reminder> findByReminderTimeBetween(Date startTime, Date endTime);

    Optional<Reminder> findById(Long id);
}