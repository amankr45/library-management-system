package com.aman.LibraryManagementSystem.mapper;

import com.aman.LibraryManagementSystem.dto.request.BookRequest;
import com.aman.LibraryManagementSystem.dto.response.BookResponse;
import com.aman.LibraryManagementSystem.entity.Book;
import com.aman.LibraryManagementSystem.util.InputSanitizer;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookRequest request) {

        return new Book(
                InputSanitizer.sanitize(request.getTitle()),
                InputSanitizer.sanitize(request.getAuthor()),
                InputSanitizer.normalizeIsbn(request.getIsbn()),
                request.getCategory()
        );
    }

    public BookResponse toResponse(Book book) {

        BookResponse response = new BookResponse();

        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setIsbn(book.getIsbn());
        response.setCategory(book.getCategory());
        response.setStatus(book.getStatus());

        return response;
    }

    public void updateEntity(BookRequest request, Book book) {

        book.setTitle(
                InputSanitizer.sanitize(request.getTitle())
        );

        book.setAuthor(
                InputSanitizer.sanitize(request.getAuthor())
        );

        book.setIsbn(
                InputSanitizer.normalizeIsbn(request.getIsbn())
        );

        book.setCategory(
                request.getCategory()
        );
    }
}