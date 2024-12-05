package com.example.demo.DONT_NEED.repositories;

import com.example.demo.entities.Note;
import com.example.demo.repositories.interfaces.CrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class NoteRepository1 implements CrudRepository<Note,Long> {

    private Long currentId= 0L;

    private synchronized long nextId()
    {
        return ++currentId;
    }

    private final ArrayList<Note> notesContainer;

    @Autowired
    public NoteRepository1(ArrayList<Note> notesContainer) {
        this.notesContainer = notesContainer;
    }

    @Override
    public void create(Note note) {


        //т.к. сейчас уже есть генератор id в бд, код отключаю.
//        if (note.getId()==null)
//        {
//            note.setId(nextId());
//        }
//        if (notesContainer.stream().anyMatch(n->n.getId().equals(note.getId())))
//        {
//            throw new IllegalArgumentException("Note with id "+note.getId()+" already exists.\n May be you wanna use Update?");
//        }
        notesContainer.add(note);
    }


    @Override
    public Note read(Long id) {
        return notesContainer.stream().filter(note -> Objects.equals(note.getId(), id))
                .findFirst()
                .orElseThrow(()-> new NoSuchElementException("No element with id "+id));

    }

    @Override
    public void update(Note newNote) {
        var note=read(newNote.getId());
        notesContainer.set(notesContainer.indexOf(note),newNote);

    }

    public void update(Long id, Note note) {
        note.setId(id);
        update(note);
    }

    @Override
    public void delete(Long id) {
        var note=read(id);
        notesContainer.remove(note);
    }


    public ArrayList<Note> getNotes() {
        return notesContainer;
    }

    public ArrayList<Note> findByTitle(String title) {
        return notesContainer.stream()
               .filter(note -> note.getTitle().toLowerCase().contains(title.toLowerCase()))
               .collect(Collectors.toCollection(ArrayList::new));
    }
}
