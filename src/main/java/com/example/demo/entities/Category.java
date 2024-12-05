package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий категорию заметок.
 * <p>
 * Каждая категория может иметь родительскую категорию и принадлежать пользователю.
 * </p>
 *
 * @author VladimirBoss
 */
@Entity
@Table(name = "categories")
@Setter
@Getter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();

    /**
     * Конструктор для создания категории без родительской категории.
     *
     * @param name Название категории.
     * @param user Пользователь, которому принадлежит категория.
     */
    public Category(String name, User user) {
        this.name = name;
        this.parent = null;
        this.user = user;
    }

    /**
     * Конструктор для создания категории с родительской категорией.
     *
     * @param name   Название категории.
     * @param parent Родительская категория.
     * @param user   Пользователь, которому принадлежит категория.
     */
    public Category(String name, Category parent, User user) {
        this.name = name;
        this.parent = parent;
        this.user = user;
    }

    /**
     * Возвращает строковое представление Категории.
     *
     * @return Строка с информацией о Категории.
     */
    @Override
    public String toString() {

        if (parent == null) {
            return name;
        }
        return parent + "->" + name;
    }
}