package com.example.demo.controllers;

import com.example.demo.constants.ViewConstants;
import com.example.demo.entities.Note;
import com.example.demo.entities.Reminder;
import com.example.demo.services.NoteService;
import com.example.demo.services.ReminderService;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * Контроллер для управления напоминаниями пользователей.
 * <p>
 * Обрабатывает запросы, связанные с добавлением, отображением и удалением напоминаний для заметок.
 *
 * @author VladimirBoss
 */
@Controller
@RequestMapping("/notes")
public class ReminderController {

    private final Logger logger = LoggerFactory.getLogger(ReminderController.class);
    @Autowired
    private ReminderService reminderService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private UserService userService;


    /**
     * Добавляет напоминание к заметке.
     *
     * @param noteId       ID заметки, к которой добавляется напоминание.
     * @param reminderTime Время напоминания в формате ISO.
     * @return Перенаправление на страницу со всеми заметками или сообщение об ошибке.
     */
    @PostMapping("/{noteId}/reminders")
    public String addReminderToNote(@PathVariable Long noteId, @RequestParam String reminderTime) {
        LocalDateTime reminderDate;
        try {
            reminderDate = LocalDateTime.parse(reminderTime);
        } catch (Exception e) {
            logger.error("Ошибка при добавлении напоминания для заметки с ID {}: {}", noteId, e.getMessage());
            return "redirect:" + ViewConstants.HOME_PAGE + "?error=invalid_date";
        }

        Reminder reminder = new Reminder();
        reminder.setTime(reminderDate);
        reminderService.addReminderToNote(noteId, reminder);
        logger.info("Добавлено напоминание для заметки с ID {}: {}", noteId, reminderDate);
        return "redirect:" + ViewConstants.HOME_PAGE;
    }

    /**
     * Отображает форму для добавления напоминания к заметке.
     *
     * @param noteId ID заметки, к которой добавляется напоминание.
     * @param model  Модель для передачи данных в представление.
     * @return Название HTML-шаблона для добавления напоминания.
     */
    @GetMapping("/{noteId}/reminders/add")
    public String showAddReminderForm(@PathVariable Long noteId, Model model) {
        Note note = noteService.getNoteById(noteId);
        model.addAttribute("note", note);
        model.addAttribute("reminder", new Reminder());
        logger.debug("Пользователь открывает форму для добавления напоминания к заметке с ID {}.", noteId);
        return ViewConstants.ADD_REMINDER_VIEW;
    }

    /**
     * Отображает форму для удаления напоминания.
     *
     * @param reminderId  ID удаляемого напоминания.
     * @param model       Модель для передачи данных в представление.
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Название HTML-шаблона для удаления напоминания.
     */
    @GetMapping("/reminders/{reminderId}/delete")
    public String showRemoveReminderForm(@PathVariable Long reminderId, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        Reminder reminder = reminderService.getReminderById(reminderId);
        if (!reminder.getNote().getUser().getUsername().equals(userDetails.getUsername())) {
            logger.warn("Попытка удаления напоминания {} для заметки, которую не создавал пользователь.", reminderId);
            return "redirect:" + ViewConstants.HOME_PAGE;
        }
        model.addAttribute("reminder", reminder);
        logger.debug("Пользователь открывает форму для удаления напоминания с ID {}.", reminderId);
        return ViewConstants.REMOVE_REMINDER_VIEW;
    }

    /**
     * Удаляет напоминание по ID.
     *
     * @param reminderId  ID удаляемого напоминания.
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Перенаправление на страницу со всеми заметками после удаления напоминания.
     */
    @PostMapping("/reminders/{reminderId}/delete")
    public String removeReminder(@PathVariable Long reminderId, @AuthenticationPrincipal UserDetails userDetails) {
        Reminder reminder = reminderService.getReminderById(reminderId);
        if (!reminder.getNote().getUser().getUsername().equals(userDetails.getUsername())) {
            logger.warn("Попытка удаления напоминания {} для заметки, которую не создавал пользователь.", reminderId);
            return "redirect:" + ViewConstants.HOME_PAGE;
        }
        reminderService.removeReminder(reminderId);
        logger.info("Напоминание с ID {} было успешно удалено пользователем {}.", reminderId, userDetails.getUsername());
        return "redirect:" + ViewConstants.HOME_PAGE;
    }
}