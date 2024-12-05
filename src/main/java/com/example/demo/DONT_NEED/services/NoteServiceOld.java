package com.example.demo.DONT_NEED.services;

import com.example.demo.DONT_NEED.repositories.NoteRepository1;
import com.example.demo.entities.Note;
import com.example.demo.DONT_NEED.services.interfaces.NoteInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class NoteServiceOld implements NoteInterface {


    private final NoteRepository1 noteRepository1;

    @Autowired
    public NoteServiceOld(NoteRepository1 noteRepository1) {
        this.noteRepository1 = noteRepository1;
    }

    @Override
    public void createNote(String title, String content,Boolean pinned) {
        noteRepository1.create(new Note(title, content,pinned));
    }

    public void createNote(String title, String text)
    {
        createNote(title, text, false);
    }

    public void createNote(Note note)
    {
        noteRepository1.create(note);
    }
    @Override
    public void deleteNote(Long id) {
        noteRepository1.delete(id);
    }

    @Override
    public void updateNote(Long id, String title, String content) {
        updateNote(new Note(id,title,content));
    }


    @Override
    public void updateNote(Note note) {
        noteRepository1.update(note);
    }

    @Override
    public Note getNoteById(Long id) {
        return noteRepository1.read(id);
    }

    @Override
    public ArrayList<Note> findNoteByTitle(String title) {
        return noteRepository1.findByTitle(title);
    }

    @Override
    public ArrayList<Note> getAllNotes() {
        return noteRepository1.getNotes();
    }

    @Override
    public Note pinNote(Long id) {
        Note note = noteRepository1.read(id);
        note.setPinned(true);
        updateNote(note);
        return note;
    }

    @Override
    public Note unpinNote(Long id) {
        Note note = noteRepository1.read(id);
        note.setPinned(false);
        updateNote(note);
        return note;
    }
}
