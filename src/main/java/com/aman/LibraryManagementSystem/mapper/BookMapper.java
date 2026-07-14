package com.aman.LibraryManagementSystem.mapper;

import com.aman.LibraryManagementSystem.dto.request.BookRequest;
import com.aman.LibraryManagementSystem.dto.response.BookResponse;
import com.aman.LibraryManagementSystem.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(
            BookRequest request
    ) {
        return new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getIsbn(),
                request.getCategory()
        );
    }

    public BookResponse toResponse(
            Book book
    ) {

        BookResponse response = new BookResponse();

        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setIsbn(book.getIsbn());
        response.setCategory(book.getCategory());
        response.setStatus(book.getStatus());

        return response;
    }

}