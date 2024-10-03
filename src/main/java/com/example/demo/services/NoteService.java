package com.example.demo.services;

import com.example.demo.note.Note;
import com.example.demo.repositories.NoteRepository;
import com.example.demo.services.interfaces.NoteInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class NoteService implements NoteInterface {


    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void createNote(String title, String content,Boolean pinned) {
        noteRepository.create(new Note(title, content,pinned));
    }

    public void createNote(String title, String content)
    {
        createNote(title, content, false);
    }

    @Override
    public void deleteNote(Long id) {
        noteRepository.delete(id);
    }

    @Override
    public void updateNote(Long id, String title, String content) {
        updateNote(new Note(id,title,content));
    }


    @Override
    public void updateNote(Note note) {
        noteRepository.update(note);
    }

    @Override
    public Note getNoteById(Long id) {
        return noteRepository.read(id);
    }

    @Override
    public ArrayList<Note> findNoteByTitle(String title) {
        return noteRepository.findByTitle(title);
    }

    @Override
    public ArrayList<Note> getAllNotes() {
        return noteRepository.getNotes();
    }

    @Override
    public Note pinNote(Long id) {
        Note note = noteRepository.read(id);
        note.setPinned(true);
        updateNote(note);
        return note;
    }

    @Override
    public Note unpinNote(Long id) {
        Note note = noteRepository.read(id);
        note.setPinned(false);
        updateNote(note);
        return note;
    }
}
