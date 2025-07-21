package com.example.books.query.controller;

import com.example.books.query.service.BookQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books/query")
public class BookQueryController {

    private final BookQueryService service;

    public BookQueryController(BookQueryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAllBooks());
    }
}
