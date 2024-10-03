package com.example.demo.note;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class Note {

    private  Long id;

    private String title;

    private String text;

    private boolean pinned;


    //private ArrayList<Tag> tags;


    public Note(String title, String text, Boolean pinned) {
        id=null;

        this.title = title;
        this.text = text;
        this.pinned = pinned;
        this.pinned=false;
    }

    public Note(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.text = content;
        this.pinned = false;
    }


    @Override
    public String toString() {
        return "Note: " +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", pinned=" + pinned +
                ' ';
    }
}
