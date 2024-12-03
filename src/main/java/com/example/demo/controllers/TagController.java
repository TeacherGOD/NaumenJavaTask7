package com.example.demo.controllers;

import com.example.demo.entities.Note;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.services.NoteService;
import com.example.demo.services.TagService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tags")
@PreAuthorize("hasRole('PREMIUM') or hasRole('ADMIN')")
public class TagController {

    private final TagService tagService;
    private final UserService userService;
    private final NoteService noteService;

    @Autowired
    public TagController(TagService tagService, UserService userService, NoteService noteService) {
        this.tagService = tagService;
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping
    public String getTags(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<Tag> tags = tagService.getTagsByUser(user);
        model.addAttribute("tags", tags);
        return "tags";
    }

    @PostMapping("/add")
    public String addTag(@RequestParam String name, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        tagService.createTag(name, user);
        return "redirect:/tags";
    }


    @PostMapping("/delete/{id}")
    public String deleteTag(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        tagService.deleteTagForUser(id, user);
        return "redirect:/tags"; // Перенаправление на страницу тегов
    }

    // Обновление тега
    @PostMapping("/update/{id}")
    public String updateTag(@PathVariable Long id, @RequestParam String newName, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        tagService.updateTagForUser(id, newName, user);
        return "redirect:/tags"; // Перенаправление на страницу тегов
    }


    @GetMapping("/filter")
    public String filterNotes(@RequestParam(required = false) List<Long> tagIds ,
                              @RequestParam(required = false) String searchTerm,
                              Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<Tag> selectedTags;
        List<Tag> tags=tagService.getTagsByUser(user);;
        List<Note> notes;
        if (tagIds == null)
        {
            selectedTags=new ArrayList<Tag>();

            notes = noteService.findNotesByUser(userService.findByUsername(userDetails.getUsername()));
        }
        else {
            // Получаем теги по их ID
            selectedTags = tagIds.stream()
                    .map(tagService::getTagById)
                    .filter(tag -> tag != null && tag.getUser().equals(user)) // Проверяем принадлежность пользователю
                    .toList();
            // Получаем заметки по выбранным тегам
            notes = noteService.getNotesByTags(selectedTags);
        }
        List<Note> notesSearch;
        if (searchTerm!= null &&!searchTerm.isEmpty()) {
            notesSearch = noteService.searchNotesByUserAndTerm(user, searchTerm);
            notes.retainAll(notesSearch);
        }

        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("user",user);
        model.addAttribute("notes", notes);
        model.addAttribute("selectedTags", selectedTags);
        model.addAttribute("tags", tags);
        return "tags-filter";
    }


}