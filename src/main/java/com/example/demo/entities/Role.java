package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс, представляющий роль пользователя.
 * <p>
 * Каждая роль может быть связана с несколькими пользователями, и
 * каждый пользователь может иметь несколько ролей.
 * </p>
 *
 * @author VladimirBoss
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название роли.
     */
    private String name;

    /**
     * Пользователи, имеющие эту роль.
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();


    /**
     * Конструктор для создания роли с заданным именем.
     *
     * @param name Название роли.
     */
    public Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}