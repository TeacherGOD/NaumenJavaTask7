package com.example.demo.controllers;

import com.example.demo.entities.Reminder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendReminder(Reminder reminder) {
        // Отправка уведомления на клиент
        messagingTemplate.convertAndSend("/topic/reminders", reminder.getNote().getTitle());
    }
}