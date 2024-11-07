package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Entity
@Table(name = "notes")
@NoArgsConstructor
@Getter
@Setter
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String title;

    private String text;

    private boolean pinned;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "note_tags",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();


    public Note(String title, String text, Boolean pinned) {
        id=null;

        this.title = title;
        this.text = text;
        this.pinned = pinned != null && pinned;
        this.tags=new ArrayList<>();
    }

    public Note(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.text = content;
        this.pinned = false;
        this.tags=new ArrayList<>();
    }


    @Override
    public String toString() {
        return "Note: " +
                "" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                (pinned ? " 📌" : "")+
                ' ';
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }
}
