package com.example.books.query.service;

import com.example.books.query.dto.BookQueryDTO;
import com.example.books.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookQueryService {
    private final BookRepository repository;

    public BookQueryService(BookRepository repository) {
        this.repository = repository;
    }

    public List<BookQueryDTO> getAllBooks() {
        return repository.findAll().stream().map(book -> {
            BookQueryDTO dto = new BookQueryDTO();
            dto.setId(book.getId());
            dto.setTitle(book.getTitle());
            dto.setAuthor(book.getAuthor());
            dto.setPublishedDate(book.getPublishedDate());
            return dto;
        }).collect(Collectors.toList());
    }
}
