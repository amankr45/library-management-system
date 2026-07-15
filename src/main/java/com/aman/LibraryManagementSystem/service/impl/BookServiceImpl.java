package com.aman.LibraryManagementSystem.service.impl;

import com.aman.LibraryManagementSystem.dto.request.BookRequest;
import com.aman.LibraryManagementSystem.dto.response.BookResponse;
import com.aman.LibraryManagementSystem.entity.Book;
import com.aman.LibraryManagementSystem.exception.book.BookNotFoundException;
import com.aman.LibraryManagementSystem.mapper.BookMapper;
import com.aman.LibraryManagementSystem.repository.BookRepository;
import com.aman.LibraryManagementSystem.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl
        implements BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    public BookServiceImpl(
            BookRepository repository,
            BookMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public BookResponse createBook(
            BookRequest request
    ) {

        Book book = mapper.toEntity(request);
        Book savedBook = repository.save(book);

        return mapper.toResponse(savedBook);
    }

    @Override
    public BookResponse getBookById(Long id) {

        Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return mapper.toResponse(book);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        throw new UnsupportedOperationException(
                "Not implemented yet."
        );
    }

    @Override
    public void deleteBook(
            Long id
    ) {
        throw new UnsupportedOperationException(
                "Not implemented yet."
        );
    }

}