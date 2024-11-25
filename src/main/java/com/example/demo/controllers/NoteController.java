package com.example.demo.controllers;

import com.example.demo.entities.Category;
import com.example.demo.entities.Note;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping("/view/all")
    public String getAllNotes(Model model,@AuthenticationPrincipal UserDetails userDetails) {

        var userName=userDetails.getUsername();
        var currentUser =userService.findByUsername(userName);

        List<Note> notes;
        if (currentUser.hasRole("ADMIN")) {
            notes = noteService.getAllNotes();
        }else {
            notes=noteService.findNotesByUser(currentUser);
        }



        model.addAttribute("notes",notes);
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

        var userName=userDetails.getUsername();
        var currentUser =userService.findByUsername(userName);

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
}
