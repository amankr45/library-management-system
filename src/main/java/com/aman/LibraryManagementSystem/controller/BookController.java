package com.aman.LibraryManagementSystem.controller;

import com.aman.LibraryManagementSystem.entity.Book;
import com.aman.LibraryManagementSystem.service.BookService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(
            BookService bookService
    ) {
        this.bookService = bookService;
    }

    @PostMapping
    public Book createBook(
            @RequestBody Book book
    ) {
        return bookService.save(book);
    }
}