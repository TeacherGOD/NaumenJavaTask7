package com.example.demo.controllers;

import com.example.demo.entities.Note;
import com.example.demo.entities.Reminder;
import com.example.demo.services.NoteService;
import com.example.demo.services.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/notes")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private NoteService noteService;

    @PostMapping("/{noteId}/reminders")
    public String addReminderToNote(@PathVariable Long noteId, @RequestParam String reminderTime) {
        LocalDateTime reminderDate;
        try {
            reminderDate = LocalDateTime.parse(reminderTime);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/notes/view/all?error=invalid_date";
        }

        Reminder reminder = new Reminder();
        reminder.setTime(reminderDate);
        reminderService.addReminderToNote(noteId, reminder);
        return "redirect:/notes/view/all"; // Перенаправление на главную страницу после добавления напоминания
    }

    @GetMapping("/{noteId}/reminders/add")
    public String showAddReminderForm(@PathVariable Long noteId, Model model) {
        Note note = noteService.getNoteById(noteId); // Получаем заметку по ID
        model.addAttribute("note", note);
        model.addAttribute("reminder", new Reminder());
        return "addReminder"; // Возвращаем шаблон для добавления напоминания
    }

    @GetMapping("/reminders/{reminderId}/delete")
    public String showRemoveReminderForm(@PathVariable Long reminderId, Model model) {
        Reminder reminder = reminderService.getReminderById(reminderId); // Получаем напоминание по ID
        model.addAttribute("reminder", reminder);
        return "removeReminder"; // Возвращаем шаблон для удаления напоминания
    }

    @PostMapping("/reminders/{reminderId}/delete")
    public String removeReminder(@PathVariable Long reminderId) {
        reminderService.removeReminder(reminderId);
        return "redirect:/notes/view/all"; // Перенаправление на главную страницу после удаления напоминания
    }
}