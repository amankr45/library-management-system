package com.aman.LibraryManagementSystem.controller;

import com.aman.LibraryManagementSystem.dto.request.BookRequest;
import com.aman.LibraryManagementSystem.dto.response.BookResponse;
import com.aman.LibraryManagementSystem.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public BookResponse createBook(
            @Valid
            @RequestBody BookRequest request
    ) {
        return bookService.createBook(request);
    }

    @GetMapping("/{id}")
    public BookResponse getBookById(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @GetMapping
    public Page<BookResponse> getAllBooks(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ){
        return bookService.getAllBooks(page, size);
    }

    @PutMapping("/{id}")
    public BookResponse updateBook(@PathVariable Long id,
                                   @Valid
                                   @RequestBody BookRequest request){
        return bookService.updateBook(id,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }
}