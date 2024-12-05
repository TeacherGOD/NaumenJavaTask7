package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс, представляющий напоминание.
 * <p>
 * Каждое напоминание связано с заметкой и содержит информацию о времени,
 * когда оно должно сработать.
 * </p>
 *
 * @author VladimirBoss
 */
@Entity
@Table(name = "reminders")
@Getter
@Setter
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Время, когда должно сработать напоминание.
     */
    private LocalDateTime time;

    /**
     * Заметка, к которой привязано это напоминание.
     */
    @OneToOne
    @JoinColumn(name = "note_id", unique = true)
    private Note note;

    /**
     * Статус активности напоминания. По умолчанию включено.
     */
    private boolean enabled = true;
}