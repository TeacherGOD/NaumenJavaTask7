package com.example.demo.controllers;

import com.example.demo.constants.ViewConstants;
import com.example.demo.entities.Category;
import com.example.demo.entities.Note;
import com.example.demo.entities.Tag;
import com.example.demo.services.CategoryService;
import com.example.demo.services.NoteService;
import com.example.demo.services.TagService;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Контроллер для управления заметками пользователей.
 * <p>
 * Обрабатывает запросы, связанные с созданием, отображением,
 * редактированием и удалением заметок.
 *
 * @author VladimirBoss
 * @see NoteService
 * @see TagService
 * @see CategoryService
 * @see UserService
 */
@Controller
@RequestMapping("/notes")
public class NoteController {

    Logger logger = LoggerFactory.getLogger(NoteController.class);
    @Autowired
    private NoteService noteService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    /**
     * Отображает все заметки текущего пользователя, или все, если он имеет роль Admin.
     *
     * @param model       Модель для передачи данных в представление.
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Название HTML-шаблона для отображения всех заметок.
     */
    @GetMapping("/view/all")
    public String getAllNotes(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        var userName = userDetails.getUsername();
        var currentUser = userService.findByUsername(userName);
        List<Note> notes;
        List<Note> pinnedNotes;
        if (currentUser.hasRole("ADMIN")) {
            notes = noteService.getAllNotes();
            pinnedNotes = noteService.findAllPinned();
            logger.info("Пользователь {} (ADMIN) запрашивает все заметки.", userName);

        } else {
            notes = noteService.findNotesByUser(currentUser);
            pinnedNotes = noteService.findPinnedByUser(currentUser);
            logger.info("Пользователь {} запрашивает свои заметки.", userName);
        }
        notes.removeAll(pinnedNotes);
        notes.addAll(0, pinnedNotes);
        model.addAttribute("notes", notes);
        model.addAttribute("user", currentUser);
        return ViewConstants.ALL_NOTES_VIEW;
    }


    /**
     * Добавляет новую заметку.
     *
     * @param note        Объект заметки, который нужно добавить.
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Перенаправление на страницу со всеми заметками.
     */
    @PostMapping("/add")
    public String addNote(@ModelAttribute Note note, @AuthenticationPrincipal UserDetails userDetails) {
        note.setUser(userService.findByUsername(userDetails.getUsername()));
        noteService.createNote(note);
        logger.info("Пользователь {} добавил новую заметку: {}", userDetails.getUsername(), note.getTitle());
        return "redirect:" + ViewConstants.HOME_PAGE;
    }

    /**
     * Отображает форму для создания новой заметки.
     *
     * @param model       Модель для передачи данных в представление.
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Название HTML-шаблона для создания заметки.
     */
    @GetMapping("/create")
    public String createNoteForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("note", new Note());
        var username = userDetails.getUsername();
        var currentUser = userService.findByUsername(username);
        List<Category> categories = categoryService.getCategoriesByUser(currentUser);
        List<Tag> tags = tagService.getTagsByUser(currentUser);
        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
        logger.debug("Пользователь {} открывает форму для создания новой заметки.", username);
        return ViewConstants.CREATE_NOTE_VIEW;
    }

    /**
     * Переключает состояние закрепления заметки по ID.
     *
     * @param id ID заметки.
     * @return Перенаправление на страницу со всеми заметками.
     */
    @PostMapping("/pin/{id}")
    public String togglePin(@PathVariable Long id,@AuthenticationPrincipal UserDetails userDetails) {
        var userName = userDetails.getUsername();
        var currentUser = userService.findByUsername(userName);
        Note note;
        try {
            note = noteService.findNote(id);
        } catch (Exception ex) {
            logger.error("Проблема при закреплении заметки с id {}: {}", id, ex.getMessage());
            return "redirect:" + ViewConstants.HOME_PAGE;
        }
        if (!Objects.equals(currentUser.getUsername(), note.getUser().getUsername())) {
            logger.warn("Попытка закрепить чужую заметку: {} пользователем: {}", id, currentUser.getUsername());
            return "redirect:" + ViewConstants.HOME_PAGE;
        }
        noteService.togglePin(id);
        logger.debug("Заметка с ID {} была переключена на закрепленную/незакрепленную.", id);
        return "redirect:" + ViewConstants.HOME_PAGE;
    }


    /**
     * Отображает форму для редактирования заметки по ID.
     *
     * @param id    ID заметки для редактирования.
     * @param model Модель для передачи данных в представление.
     * @return Название HTML-шаблона для редактирования заметки или перенаправление, если заметка не найдена.
     */
    @GetMapping("/edit/{id}")
    public String editNoteForm(@PathVariable Long id, Model model,@AuthenticationPrincipal UserDetails userDetails) {
        Note note = noteService.getNoteById(id);
        if (note == null) {
            logger.warn("Попытка редактирования несуществующей заметки с ID {}.", id);
            return "redirect:" + ViewConstants.HOME_PAGE;
        }
        var userName = userDetails.getUsername();
        var currentUser = userService.findByUsername(userName);
        if (!Objects.equals(currentUser.getUsername(), note.getUser().getUsername())) {
            logger.warn("Попытка редактировать чужую заметку: {} пользователем: {}", id, currentUser.getUsername());
            return "redirect:" + ViewConstants.HOME_PAGE;
        }
        var user = note.getUser();
        var tags = tagService.getTagsByUser(user);
        var categories = categoryService.getCategoriesByUser(user);
        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
        model.addAttribute("note", note);
        logger.debug("Пользователь {} открывает форму редактирования для заметки с ID {}.", user.getUsername(), id);
        return ViewConstants.EDIT_NOTE_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String updateNote(@PathVariable Long id, @ModelAttribute Note note,@AuthenticationPrincipal UserDetails userDetails) {
        var userName = userDetails.getUsername();
        var currentUser = userService.findByUsername(userName);
        if (!Objects.equals(currentUser.getUsername(), note.getUser().getUsername())) {
            logger.warn("Попытка редактировать чужую заметку: {} пользователем: {}", id, currentUser.getUsername());
            return "redirect:" + ViewConstants.HOME_PAGE;
        }
        noteService.updateNote(id, note.getTitle(), note.getText(), note.getCategory(), note.getTags());
        logger.debug("Заметка с ID {} была обновлена.", id);
        return "redirect:" + ViewConstants.HOME_PAGE; // Перенаправление после успешного обновления
    }

    /**
     * Удаляет заметку по ID.
     *
     * @param id          ID удаляемой заметки.
     * @param userDetails Данные аутентифицированного пользователя.
     * @return Перенаправление на страницу со всеми заметками после удаления.
     */
    @PostMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        var userName = userDetails.getUsername();
        var currentUser = userService.findByUsername(userName);
        Note note;
        try {
            note = noteService.findNote(id);
        } catch (Exception ex) {
            logger.error("Проблема при удалении заметки с id {}: {}", id, ex.getMessage());
            return "redirect:" + ViewConstants.HOME_PAGE;
        }
        if (!Objects.equals(currentUser.getUsername(), note.getUser().getUsername())) {
            logger.warn("Попытка удалить чужую заметку: {} пользователем: {}", id, currentUser.getUsername());
            return "redirect:" + ViewConstants.HOME_PAGE;
        }
        noteService.deleteNote(id);
        logger.info("Пользователь {} удалил заметку с ID {}.", currentUser.getUsername(), id);
        return "redirect:" + ViewConstants.HOME_PAGE;
    }
}
