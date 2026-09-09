package com.aman.LibraryManagementSystem.service.impl;

import com.aman.LibraryManagementSystem.dto.request.BookRequest;
import com.aman.LibraryManagementSystem.dto.response.BookPageResponse;
import com.aman.LibraryManagementSystem.dto.response.BookResponse;
import com.aman.LibraryManagementSystem.entity.Book;
import com.aman.LibraryManagementSystem.enums.BookCategory;
import com.aman.LibraryManagementSystem.enums.BookStatus;
import com.aman.LibraryManagementSystem.exception.book.BookNotFoundException;
import com.aman.LibraryManagementSystem.exception.book.DuplicateBookException;
import com.aman.LibraryManagementSystem.mapper.BookMapper;
import com.aman.LibraryManagementSystem.repository.BookRepository;
import com.aman.LibraryManagementSystem.repository.specification.BookSpecifications;
import com.aman.LibraryManagementSystem.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

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
    public BookResponse createBook(BookRequest request) {

        Book book = mapper.toEntity(request);

        if (repository.existsByIsbn(book.getIsbn())) {
            throw new DuplicateBookException(book.getIsbn());
        }

        Book savedBook = repository.save(book);

        return mapper.toResponse(savedBook);
    }

    @Override
    public BookResponse getBookById(Long id) {

        Book book = repository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(id)
                );

        return mapper.toResponse(book);
    }

    @Override
    public BookPageResponse getAllBooks(
            String search,
            BookCategory category,
            BookStatus status,
            Pageable pageable
    ) {

        Specification<Book> specification =
                Specification.allOf(
                        BookSpecifications.search(search),
                        BookSpecifications.hasCategory(category),
                        BookSpecifications.hasStatus(status)
                );

        Page<Book> books = repository.findAll(
                specification,
                pageable
        );

        List<BookResponse> content = books.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return new BookPageResponse(
                content,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public BookResponse updateBook(
            Long id,
            BookRequest request
    ) {

        Book book = repository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(id)
                );

        mapper.updateEntity(request, book);

        Book updatedBook = repository.save(book);

        return mapper.toResponse(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {

        Book book = repository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(id)
                );

        repository.delete(book);
    }
}