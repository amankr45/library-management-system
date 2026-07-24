package com.aman.LibraryManagementSystem.service;

import com.aman.LibraryManagementSystem.dto.request.BookRequest;
import com.aman.LibraryManagementSystem.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BookService {

    BookResponse createBook(BookRequest request);

    BookResponse getBookById(Long id);

    Page<BookResponse> getAllBooks(Pageable pageable);

    BookResponse updateBook(Long id,BookRequest request);

    void deleteBook(Long id);

}