package com.example.demo.controllers;

import com.example.demo.entities.Category;
import com.example.demo.entities.Note;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;


    @Autowired
    private TagService tagService; // Сервис для работы с тегами

    @Autowired
    private CategoryService categoryService; // Сервис для работы с категориями

    @Autowired
    private UserService userService; // Сервис для работы с пользователями

    @GetMapping("/view/all")
    public String getAllNotes(Model model) {
        // Display all notes
        Iterable<Note> notes = noteService.getAllNotes();

        model.addAttribute("notes",notes);
        return "all-notes";
    }


    @PostMapping("/add")
    public String addNote(@ModelAttribute Note note) {
        noteService.createNote(note);
        return "redirect:/notes/view/all"; // Перенаправление после добавления заметки
    }

    @GetMapping("/create")
    public String createNoteForm(Model model) {
        model.addAttribute("note", new Note());

        // Получаем списки тегов, категорий и пользователей
        List<Tag> tags = tagService.getAllTags(); // Предполагается, что метод getAllTags() существует
        List<Category> categories = categoryService.getAllCategories(); // Предполагается, что метод getAllCategories() существует
        List<User> users = userService.getAllUsers(); // Предполагается, что метод getAllUsers() существует

        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
        model.addAttribute("users", users);

        return "create-note";
    }
}
