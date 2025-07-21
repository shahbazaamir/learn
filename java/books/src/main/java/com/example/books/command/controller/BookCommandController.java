package com.example.books.command.controller;

import com.example.books.command.dto.BookCommandDTO;
import com.example.books.command.service.BookCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books/command")
public class BookCommandController {

    private final BookCommandService service;

    public BookCommandController(BookCommandService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BookCommandDTO dto) {
        return ResponseEntity.ok(service.createBook(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
