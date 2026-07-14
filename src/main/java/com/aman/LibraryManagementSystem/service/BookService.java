package com.aman.LibraryManagementSystem.service;

import com.aman.LibraryManagementSystem.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book save(Book book);

    Optional<Book> findById(Long id);

    List<Book> findAll();

    void deleteById(Long id);

}