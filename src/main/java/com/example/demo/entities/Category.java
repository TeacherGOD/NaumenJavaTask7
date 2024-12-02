package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Setter
@Getter
@NoArgsConstructor
public class Category {
    public Category(String name,User user) {
        this.name = name;
        this.parent = null;
        this.user=user;
    }

    public Category(String name, Category parent,User user) {
        this.name = name;
        this.parent = parent;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne // Связь с пользователем
    @JoinColumn(name = "user_id", nullable = false) // Обязательное поле
    private User user; // Пользователь, которому принадлежит категория

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;


    @Override
    public String toString() {

        if (parent == null) {
            return name;
        }
        return parent+"->"+name ;
    }
}