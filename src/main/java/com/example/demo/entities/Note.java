package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


/**
 * Класс, представляющий заметку.
 * <p>
 * Каждая заметка может принадлежать пользователю, иметь категорию,
 * теги и напоминание.
 * </p>
 *
 * @author VladimirBoss
 */
@AllArgsConstructor
@Entity
@Table(name = "notes")
@NoArgsConstructor
@Getter
@Setter
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String text;

    private boolean pinned;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id")
    private Category category;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "note_tags",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();


    @OneToOne(mappedBy = "note", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Reminder reminder = null;

    /**
     * Конструктор для создания заметки с заданными заголовком, текстом и статусом закрепления.
     *
     * @param title  Заголовок заметки.
     * @param text   Текст заметки.
     * @param pinned Статус закрепления заметки.
     */
    public Note(String title, String text, Boolean pinned) {
        id = null;
        this.title = title;
        this.text = text;
        this.pinned = pinned != null && pinned;
        this.tags = new ArrayList<>();
        this.reminder = null;
    }

    /**
     * Конструктор для создания заметки с заданным ID, заголовком и текстом.
     *
     * @param id      ID заметки.
     * @param title   Заголовок заметки.
     * @param content Текст заметки.
     */
    public Note(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.text = content;
        this.pinned = false;
        this.tags = new ArrayList<>();
    }


    /**
     * Возвращает строковое представление заметки.
     *
     * @return Строка с информацией о заметке.
     */
    @Override
    public String toString() {
        return "Note: " + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                (pinned ? " 📌" : "") +
                ' ';
    }

    /**
     * Добавляет тег к заметке.
     *
     * @param tag Тег, который нужно добавить к заметке.
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }
}
