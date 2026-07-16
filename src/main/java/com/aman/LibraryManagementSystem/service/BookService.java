package com.aman.LibraryManagementSystem.service;

import com.aman.LibraryManagementSystem.dto.request.BookRequest;
import com.aman.LibraryManagementSystem.dto.response.BookResponse;
import org.springframework.data.domain.Page;


public interface BookService {

    BookResponse createBook(BookRequest request);

    BookResponse getBookById(Long id);

    Page<BookResponse> getAllBooks(int page,int size);

    BookResponse updateBook(Long id,BookRequest request);

    void deleteBook(Long id);

}