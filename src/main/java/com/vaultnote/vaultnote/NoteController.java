package com.vaultnote.vaultnote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        note.setOwner(username);
        return noteRepository.save(note);
    }

    @GetMapping
    public List<Note> getMyNotes() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return noteRepository.findByOwner(username);
    }
}