package com.example.demo.controllers;

import com.example.demo.DemoApplication;
import com.example.demo.entities.Category;
import com.example.demo.entities.Note;
import com.example.demo.entities.Tag;
import com.example.demo.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;


    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;


    Logger logger = LoggerFactory.getLogger(NoteController.class);

    @GetMapping("/view/all")
    public String getAllNotes(Model model,@AuthenticationPrincipal UserDetails userDetails) {

        var userName=userDetails.getUsername();
        var currentUser =userService.findByUsername(userName);

        List<Note> notes;
        List<Note> pinnedNotes;
        if (currentUser.hasRole("ADMIN")) {
            notes = noteService.getAllNotes();
            pinnedNotes=noteService.findAllPinned();
        }else {
            notes=noteService.findNotesByUser(currentUser);
            pinnedNotes=noteService.findPinnedByUser(currentUser);
        }
        notes.removeAll(pinnedNotes);
        notes.addAll(0,pinnedNotes);

        model.addAttribute("notes",notes);
        model.addAttribute("user",currentUser);
        return "all-notes";
    }


    @PostMapping("/add")
    public String addNote(@ModelAttribute Note note, @AuthenticationPrincipal UserDetails userDetails) {

        note.setUser(userService.findByUsername(userDetails.getUsername()));
        noteService.createNote(note);

        return "redirect:/notes/view/all";
    }

    @GetMapping("/create")
    public String createNoteForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("note", new Note());

        var username=userDetails.getUsername();
        var currentUser =userService.findByUsername(username);

        List<Category> categories=categoryService.getCategoriesByUser(currentUser);
        List<Tag> tags=tagService.getTagsByUser(currentUser);


//        List<Tag> tags = tagService.getAllTags();
//        List<Category> categories = categoryService.getAllCategories();
//        List<User> users = userService.getAllUsers();

        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
//        model.addAttribute("users", users);

        return "create-note";
    }


    @PostMapping("/pin/{id}")
    public String togglePin(@PathVariable Long id) {
        noteService.togglePin(id);

        return "redirect:/notes/view/all"; // Перенаправляем обратно на страницу со всеми заметками
    }


    @GetMapping("/edit/{id}")
    public String editNoteForm(@PathVariable Long id, Model model) {
        Note note = noteService.getNoteById(id);
        if (note == null) {
            return "redirect:/notes/view/all"; // Перенаправление, если заметка не найдена
        }
        var user=note.getUser();

        var tags=tagService.getTagsByUser(user);
        var categories=categoryService.getCategoriesByUser(user);

        model.addAttribute("tags",tags);
        model.addAttribute("categories", categories);
        model.addAttribute("note", note);
        return "edit-note"; // Возвращаем имя шаблона для редактирования
    }

    @PostMapping("/edit/{id}")
    public String updateNote(@PathVariable Long id, @ModelAttribute Note note) {
        noteService.updateNote(id, note.getTitle(), note.getText(), note.getCategory(), note.getTags());
        return "redirect:/notes/view/all"; // Перенаправление после успешного обновления
    }


    @PostMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        var userName=userDetails.getUsername();
        var currentUser =userService.findByUsername(userName);
        var note = noteService.findNote(id);
        if (!Objects.equals(currentUser.getUsername(), note.getUser().getUsername()))
        {
            return "redirect:/notes/view/all";
        }
//        logger.info("delete note NOW");
        noteService.deleteNote(id);
//        logger.info("Deleted, i think");





        return "redirect:/notes/view/all";
    }
}
