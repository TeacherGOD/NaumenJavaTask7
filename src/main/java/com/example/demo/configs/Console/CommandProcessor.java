package com.example.demo.configs.Console;

import com.example.demo.entities.Note;
import com.example.demo.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class CommandProcessor {


    private final NoteService noteService;

    @Autowired
    public CommandProcessor(NoteService noteService) {
        this.noteService = noteService;
    }


    public void processCommand(String command) {

        String[] parts = command.split(" !#! ");

        ArrayList<Note> notes;
        Note note;
        switch (parts[0].toLowerCase()) {
            case "add":
                noteService.createNote(parts[1], parts[2]);
                System.out.println("Note added");
                break;
            case "delete":
                noteService.deleteNote(Long.parseLong(parts[1]));
                System.out.println("Note deleted");
                break;
            case "update":
                noteService.updateNote(Long.parseLong(parts[1]), parts[2],parts[3]);
                System.out.println("Note updated");
                break;
            case "list":
                notes=noteService.getAllNotes();
                notes.stream().forEach(note1 -> System.out.println(note1));
                break;
            case "find":
                notes=noteService.findNoteByTitle(parts[1]);
                notes.stream().forEach(note1 -> System.out.println(note1));
                break;
            case "findbyid":
                note=noteService.getNoteById(Long.parseLong(parts[1]));
                System.out.println(note);
                break;
            case "pin":
                note=noteService.pinNote(Long.parseLong(parts[1]));
                System.out.println("note was pinned");
                System.out.println("\n"+note);
                break;
            case "unpin":
                note=noteService.unpinNote(Long.parseLong(parts[1]));
                System.out.println("note was unpinned");
                System.out.println("\n"+note);
                break;
            case "help":
                System.out.println("Type commands to perform actions on notes");
                System.out.println("Separator for ALL COMMANDS:' !#! '");
                System.out.println("Available commands:");
                System.out.println("  list - List all notes");
                System.out.println("  add <title> <content> - Create a new note");
                System.out.println("  update <id> <title> <content> - Update a note by ID");
                System.out.println("  delete <id> - Delete a note by ID");
                System.out.println("  find <query> - Search notes by title");
                System.out.println("  findById <id> - Search notes by title");
                System.out.println("  pin <id> - Pin a note");
                System.out.println("  unpin <id> - Unpin a note");
                System.out.println("  help - Display this help message");
                System.out.println("  exit - Exit the program");
                System.out.println("Separator for ALL COMMANDS:' !#! '");
                break;
            default:
                System.out.println("Unknown command");
        }
    }

}
