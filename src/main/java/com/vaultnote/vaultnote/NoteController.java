package com.vaultnote.vaultnote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note updatedNote) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Note> existingNote = noteRepository.findById(id);

        if (existingNote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Note note = existingNote.get();

        if (!note.getOwner().equals(username)) {
            return ResponseEntity.status(403).build();
        }

        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        noteRepository.save(note);

        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Note> existingNote = noteRepository.findById(id);

        if (existingNote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!existingNote.get().getOwner().equals(username)) {
            return ResponseEntity.status(403).build();
        }

        noteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}