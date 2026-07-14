package com.aman.LibraryManagementSystem.service.impl;

import com.aman.LibraryManagementSystem.entity.Book;
import com.aman.LibraryManagementSystem.repository.BookRepository;
import com.aman.LibraryManagementSystem.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl
        implements BookService {

    private final BookRepository repository;

    public BookServiceImpl(
            BookRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}