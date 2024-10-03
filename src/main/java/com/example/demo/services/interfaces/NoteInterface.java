package com.example.demo.services.interfaces;

import com.example.demo.note.Note;

import java.util.ArrayList;

public interface NoteInterface {

    void createNote(String title, String content, Boolean pinned);

    void deleteNote(Long id);

    void updateNote(Long id, String title, String content);

    void updateNote(Note note);

    Note getNoteById(Long id);

    ArrayList<Note> findNoteByTitle(String title);

    ArrayList<Note> getAllNotes();


    public Note unpinNote(Long id);

    public Note pinNote(Long id);
}
