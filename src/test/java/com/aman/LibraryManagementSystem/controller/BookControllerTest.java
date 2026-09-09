package com.aman.LibraryManagementSystem.controller;

import com.aman.LibraryManagementSystem.dto.request.BookRequest;
import com.aman.LibraryManagementSystem.dto.response.BookPageResponse;
import com.aman.LibraryManagementSystem.dto.response.BookResponse;
import com.aman.LibraryManagementSystem.enums.BookCategory;
import com.aman.LibraryManagementSystem.enums.BookStatus;
import com.aman.LibraryManagementSystem.exception.book.BookNotFoundException;
import com.aman.LibraryManagementSystem.exception.book.DuplicateBookException;
import com.aman.LibraryManagementSystem.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import com.aman.LibraryManagementSystem.config.SecurityConfig;
import com.aman.LibraryManagementSystem.security.CustomUserDetailsService;
import org.springframework.context.annotation.Import;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    private BookResponse createBookResponse() {

        BookResponse response = new BookResponse();

        response.setId(1L);
        response.setTitle("Clean Code");
        response.setAuthor("Robert C. Martin");
        response.setIsbn("9780132350884");
        response.setCategory(BookCategory.PROGRAMMING);
        response.setStatus(BookStatus.AVAILABLE);

        return response;
    }

    @Test
    void shouldCreateBookSuccessfully() throws Exception {

        BookResponse response = createBookResponse();

        when(bookService.createBook(any(BookRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/books")
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                          "title": "Clean Code",
                          "author": "Robert C. Martin",
                          "isbn": "9780132350884",
                          "category": "PROGRAMMING"
                        }
                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));

        verify(bookService)
                .createBook(any(BookRequest.class));
    }

    @Test
    void shouldGetBookByIdSuccessfully() throws Exception {

        when(bookService.getBookById(1L))
                .thenReturn(createBookResponse());

        mockMvc.perform(
                        get("/books/{id}", 1L)
                                .with(jwt())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.isbn")
                        .value("9780132350884"));

        verify(bookService)
                .getBookById(1L);
    }

    @Test
    void shouldGetAllBooksSuccessfully() throws Exception {

        BookResponse bookResponse =
                createBookResponse();

        BookPageResponse pageResponse =
                new BookPageResponse(
                        List.of(bookResponse),
                        0,
                        10,
                        1,
                        1,
                        true,
                        true
                );

        when(bookService.getAllBooks(
                eq(null),
                eq(null),
                eq(null),
                any(Pageable.class)
        )).thenReturn(pageResponse);

        mockMvc.perform(
                        get("/books")
                                .with(jwt())
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id")
                        .value(1))
                .andExpect(jsonPath("$.content[0].title")
                        .value("Clean Code"))
                .andExpect(jsonPath("$.content[0].isbn")
                        .value("9780132350884"))
                .andExpect(jsonPath("$.content[0].status")
                        .value("AVAILABLE"))
                .andExpect(jsonPath("$.page")
                        .value(0))
                .andExpect(jsonPath("$.size")
                        .value(10))
                .andExpect(jsonPath("$.totalElements")
                        .value(1))
                .andExpect(jsonPath("$.totalPages")
                        .value(1))
                .andExpect(jsonPath("$.first")
                        .value(true))
                .andExpect(jsonPath("$.last")
                        .value(true));

        verify(bookService).getAllBooks(
                eq(null),
                eq(null),
                eq(null),
                any(Pageable.class)
        );
    }

    @Test
    void shouldSearchBooksSuccessfully() throws Exception {

        BookResponse bookResponse =
                createBookResponse();

        BookPageResponse pageResponse =
                new BookPageResponse(
                        List.of(bookResponse),
                        0,
                        12,
                        1,
                        1,
                        true,
                        true
                );

        when(bookService.getAllBooks(
                eq("clean"),
                eq(null),
                eq(null),
                any(Pageable.class)
        )).thenReturn(pageResponse);

        mockMvc.perform(
                        get("/books")
                                .with(jwt())
                                .param("search", "clean")
                                .param("page", "0")
                                .param("size", "12")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title")
                        .value("Clean Code"))
                .andExpect(jsonPath("$.totalElements")
                        .value(1));

        verify(bookService).getAllBooks(
                eq("clean"),
                eq(null),
                eq(null),
                any(Pageable.class)
        );
    }

    @Test
    void shouldFilterBooksByCategorySuccessfully()
            throws Exception {

        BookResponse bookResponse =
                createBookResponse();

        BookPageResponse pageResponse =
                new BookPageResponse(
                        List.of(bookResponse),
                        0,
                        12,
                        1,
                        1,
                        true,
                        true
                );

        when(bookService.getAllBooks(
                eq(null),
                eq(BookCategory.PROGRAMMING),
                eq(null),
                any(Pageable.class)
        )).thenReturn(pageResponse);

        mockMvc.perform(
                        get("/books")
                                .with(jwt())
                                .param(
                                        "category",
                                        "PROGRAMMING"
                                )
                                .param("page", "0")
                                .param("size", "12")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.content[0].category"
                ).value("PROGRAMMING"));

        verify(bookService).getAllBooks(
                eq(null),
                eq(BookCategory.PROGRAMMING),
                eq(null),
                any(Pageable.class)
        );
    }

    @Test
    void shouldFilterBooksByStatusSuccessfully()
            throws Exception {

        BookResponse bookResponse =
                createBookResponse();

        BookPageResponse pageResponse =
                new BookPageResponse(
                        List.of(bookResponse),
                        0,
                        12,
                        1,
                        1,
                        true,
                        true
                );

        when(bookService.getAllBooks(
                eq(null),
                eq(null),
                eq(BookStatus.AVAILABLE),
                any(Pageable.class)
        )).thenReturn(pageResponse);

        mockMvc.perform(
                        get("/books")
                                .with(jwt())
                                .param(
                                        "status",
                                        "AVAILABLE"
                                )
                                .param("page", "0")
                                .param("size", "12")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.content[0].status"
                ).value("AVAILABLE"));

        verify(bookService).getAllBooks(
                eq(null),
                eq(null),
                eq(BookStatus.AVAILABLE),
                any(Pageable.class)
        );
    }

    @Test
    void shouldFilterBooksBySearchCategoryAndStatus()
            throws Exception {

        BookResponse bookResponse =
                createBookResponse();

        BookPageResponse pageResponse =
                new BookPageResponse(
                        List.of(bookResponse),
                        0,
                        12,
                        1,
                        1,
                        true,
                        true
                );

        when(bookService.getAllBooks(
                eq("clean"),
                eq(BookCategory.PROGRAMMING),
                eq(BookStatus.AVAILABLE),
                any(Pageable.class)
        )).thenReturn(pageResponse);

        mockMvc.perform(
                        get("/books")
                                .with(jwt())
                                .param("search", "clean")
                                .param(
                                        "category",
                                        "PROGRAMMING"
                                )
                                .param(
                                        "status",
                                        "AVAILABLE"
                                )
                                .param("page", "0")
                                .param("size", "12")
                                .param(
                                        "sort",
                                        "title,asc"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.content[0].title"
                ).value("Clean Code"))
                .andExpect(jsonPath(
                        "$.content[0].category"
                ).value("PROGRAMMING"))
                .andExpect(jsonPath(
                        "$.content[0].status"
                ).value("AVAILABLE"));

        verify(bookService).getAllBooks(
                eq("clean"),
                eq(BookCategory.PROGRAMMING),
                eq(BookStatus.AVAILABLE),
                any(Pageable.class)
        );
    }

    @Test
    void shouldUpdateBookSuccessfully()
            throws Exception {

        BookResponse response =
                createBookResponse();

        response.setTitle("Clean Code Updated");

        when(bookService.updateBook(
                eq(1L),
                any(BookRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/books/{id}", 1L)
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                          "title": "Clean Code Updated",
                          "author": "Robert C. Martin",
                          "isbn": "9780132350884",
                          "category": "PROGRAMMING"
                        }
                        """)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.title")
                                .value("Clean Code Updated")
                );

        verify(bookService).updateBook(
                eq(1L),
                any(BookRequest.class)
        );
    }

    @Test
    void shouldDeleteBookSuccessfully()
            throws Exception {

        doNothing()
                .when(bookService)
                .deleteBook(1L);

        mockMvc.perform(
                        delete("/books/{id}", 1L)
                                .with(jwt())
                )
                .andExpect(status().isNoContent());

        verify(bookService)
                .deleteBook(1L);
    }

    @Test
    void shouldReturnNotFoundWhenBookDoesNotExist()
            throws Exception {

        when(bookService.getBookById(999L))
                .thenThrow(
                        new BookNotFoundException(999L)
                );

        mockMvc.perform(
                        get("/books/{id}", 999L).with(jwt())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status")
                        .value(404))
                .andExpect(jsonPath("$.message")
                        .value("Book not found : 999"))
                .andExpect(jsonPath("$.path")
                        .value("/books/999"));
    }

    @Test
    void shouldReturnBadRequestForInvalidBookRequest()
            throws Exception {

        mockMvc.perform(
                        post("/books")
                                .with(jwt())
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content("""
                                    {
                                      "title": "",
                                      "author": "",
                                      "isbn": "invalid",
                                      "category": null
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"))
                .andExpect(
                        jsonPath("$.fieldErrors.title")
                                .value("Title is required")
                )
                .andExpect(
                        jsonPath("$.fieldErrors.isbn")
                                .value("Invalid ISBN")
                );
    }

    @Test
    void shouldReturnConflictWhenIsbnAlreadyExists()
            throws Exception {

        when(bookService.createBook(
                any(BookRequest.class)
        )).thenThrow(
                new DuplicateBookException(
                        "ISBN already exists"
                )
        );

        mockMvc.perform(
                        post("/books")
                                .with(jwt())
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content("""
                                        {
                                          "title": "Clean Code",
                                          "author": "Robert C. Martin",
                                          "isbn": "9780132350884",
                                          "category": "PROGRAMMING"
                                        }
                                        """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status")
                        .value(409))
                .andExpect(
                        jsonPath("$.message")
                                .value("ISBN already exists")
                );
    }
}