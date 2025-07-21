package com.example.books.command.service;

import com.example.books.command.dto.BookCommandDTO;
import com.example.books.command.entity.Book;
import com.example.books.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookCommandService {
    private final BookRepository repository;

    public BookCommandService(BookRepository repository) {
        this.repository = repository;
    }

    public Book createBook(BookCommandDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublishedDate(dto.getPublishedDate());
        return repository.save(book);
    }

    public void deleteBook(Long id) {
        repository.deleteById(id);
    }
}
