package com.example.demo.controllers;

import com.example.demo.entities.Reminder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Контроллер для отправки уведомлений пользователям.
 * <p>
 * Обрабатывает отправку напоминаний через WebSocket.
 *
 * @author VladimirBoss
 */
@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;
    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);


    /**
     * Конструктор для инициализации NotificationController.
     *
     * @param messagingTemplate Шаблон для отправки сообщений через WebSocket.
     */
    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Отправляет напоминание пользователям.
     *
     * @param reminder Напоминание, которое нужно отправить.
     */
    public void sendReminder(Reminder reminder) {
        messagingTemplate.convertAndSend("/topic/reminders", reminder.getNote().getTitle());
        logger.debug("Отправлено напоминание: {}", reminder.getNote().getTitle());

    }
}