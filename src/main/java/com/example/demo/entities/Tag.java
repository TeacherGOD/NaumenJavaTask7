package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс, представляющий тег для заметок.
 * <p>
 * Каждый тег может принадлежать пользователю и быть связан с несколькими заметками.
 * </p>
 *
 * @author VladimirBoss
 */
@Entity
@Table(name = "tags")
@Setter
@Getter
@NoArgsConstructor

public class Tag {


    /**
     * Список заметок, связанны с этим тегом.
     */
    @ManyToMany(mappedBy = "tags", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Note> notes = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Название тега.
     */
    private String name;
    /**
     * Пользователь, которому принадлежит тег.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Обязательное поле
    private User user;

    public Tag(String name, User user) {
        this.name = name;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}