package com.example.demo.controllers;

import com.example.demo.constants.ViewConstants;
import com.example.demo.entities.Note;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.services.NoteService;
import com.example.demo.services.TagService;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для управления тегами пользователей.
 * <p>
 * Обрабатывает запросы, связанные с добавлением, удалением, обновлением
 * и фильтрацией тегов.
 *
 * @author VladimirBoss
 */
@Controller
@RequestMapping("/tags")

public class TagController {

    private final TagService tagService;
    private final UserService userService;
    private final NoteService noteService;
    private final Logger logger = LoggerFactory.getLogger(TagController.class);


    @Autowired
    public TagController(TagService tagService, UserService userService, NoteService noteService) {
        this.tagService = tagService;
        this.userService = userService;
        this.noteService = noteService;
    }

    /**
     * Отображает все теги текущего пользователя.
     *
     * @param userDetails Данные аутентифицированного пользователя.
     * @param model       Модель для передачи данных в представление.
     * @return Название HTML-шаблона для отображения тегов.
     */
    @GetMapping
    @PreAuthorize("hasRole('PREMIUM') or hasRole('ADMIN')")
    public String getTags(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<Tag> tags = tagService.getTagsByUser(user);
        model.addAttribute("tags", tags);
        logger.debug("Пользователь {} запрашивает свои теги.", userDetails.getUsername());
        return ViewConstants.TAGS_VIEW;
    }

    /**
     * Добавляет новый тег для текущего пользователя.
     *
     * @param name        Название нового тега.
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Перенаправление на страницу тегов.
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('PREMIUM') or hasRole('ADMIN')")
    public String addTag(@RequestParam String name, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        tagService.createTag(name, user);
        logger.info("Пользователь {} добавил новый тег: {}", userDetails.getUsername(), name);
        return "redirect:/" + ViewConstants.TAGS_VIEW;
    }


    /**
     * Удаляет тег для текущего пользователя.
     *
     * @param id          ID удаляемого тега.
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Перенаправление на страницу тегов.
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('PREMIUM') or hasRole('ADMIN')")
    public String deleteTag(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        tagService.deleteTagForUser(id, user);
        logger.info("Пользователь {} удалил тег с ID {}.", userDetails.getUsername(), id);
        return "redirect:/" + ViewConstants.TAGS_VIEW; // Перенаправление на страницу тегов
    }

    // Обновление тега

    /**
     * Обновляет название тега для текущего пользователя.
     *
     * @param id          ID обновляемого тега.
     * @param newName     Новое название тега.
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Перенаправление на страницу тегов.
     */
    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('PREMIUM') or hasRole('ADMIN')")
    public String updateTag(@PathVariable Long id, @RequestParam String newName, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        tagService.updateTagForUser(id, newName, user);
        logger.info("Пользователь {} обновил тег с ID {} на новое название: {}", userDetails.getUsername(), id, newName);
        return "redirect:/" + ViewConstants.TAGS_VIEW;
    }


    /**
     * Фильтрует заметки по выбранным тегам и/или поисковому запросу.
     *
     * @param tagIds      Список ID выбранных тегов.
     * @param searchTerm  Поисковый запрос для фильтрации заметок.
     * @param model       Модель для передачи данных в представление.
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Название HTML-шаблона для отображения отфильтрованных заметок.
     */
    @GetMapping("/filter")
    public String filterNotes(@RequestParam(required = false) List<Long> tagIds,
                              @RequestParam(required = false) String searchTerm,
                              Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<Tag> selectedTags;
        List<Tag> tags = tagService.getTagsByUser(user);
        List<Note> notes;
        if (tagIds == null) {
            selectedTags = new ArrayList<Tag>();
            notes = noteService.findNotesByUser(userService.findByUsername(userDetails.getUsername()));
            logger.debug("Пользователь {} не выбрал теги. Загружаются все заметки.", userDetails.getUsername());
        } else {
            selectedTags = tagIds.stream()
                    .map(tagService::getTagById)
                    .filter(tag -> tag != null && tag.getUser().equals(user))
                    .toList();
            notes = noteService.getNotesByTags(selectedTags);
            logger.debug("Пользователь {} выбрал теги: {}. Загружаются заметки по выбранным тегам.",
                    userDetails.getUsername(), selectedTags);
        }
        List<Note> notesSearch;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            notesSearch = noteService.searchNotesByUserAndTerm(user, searchTerm);
            notes.retainAll(notesSearch);
            logger.debug("Пользователь {} ищет заметки по запросу: {}.", userDetails.getUsername(), searchTerm);
        }
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("user", user);
        model.addAttribute("notes", notes);
        model.addAttribute("selectedTags", selectedTags);
        model.addAttribute("tags", tags);
        return ViewConstants.TAGS_FILTER_VIEW;
    }


}