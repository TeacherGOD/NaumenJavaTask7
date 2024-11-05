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
    public Category(String name) {
        this.name = name;
        this.parent = null;
    }

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

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