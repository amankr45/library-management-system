package com.aman.LibraryManagementSystem.service;

import com.aman.LibraryManagementSystem.dto.request.BookRequest;
import com.aman.LibraryManagementSystem.dto.response.BookPageResponse;
import com.aman.LibraryManagementSystem.dto.response.BookResponse;
import com.aman.LibraryManagementSystem.enums.BookCategory;
import com.aman.LibraryManagementSystem.enums.BookStatus;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookResponse createBook(BookRequest request);

    BookResponse getBookById(Long id);

    BookPageResponse getAllBooks(
            String search,
            BookCategory category,
            BookStatus status,
            Pageable pageable
    );

    BookResponse updateBook(
            Long id,
            BookRequest request
    );

    void deleteBook(Long id);
}