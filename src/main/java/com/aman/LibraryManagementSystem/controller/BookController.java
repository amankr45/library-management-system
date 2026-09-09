package com.aman.LibraryManagementSystem.controller;

import com.aman.LibraryManagementSystem.dto.request.BookRequest;
import com.aman.LibraryManagementSystem.dto.response.BookPageResponse;
import com.aman.LibraryManagementSystem.dto.response.BookResponse;
import com.aman.LibraryManagementSystem.enums.BookCategory;
import com.aman.LibraryManagementSystem.enums.BookStatus;
import com.aman.LibraryManagementSystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Books",
        description = "Endpoints for managing library books"
)
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(
            summary = "Create a book",
            description = "Adds a new book to the library catalogue."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(
            @Valid
            @RequestBody
            BookRequest request
    ) {
        return bookService.createBook(request);
    }

    @Operation(
            summary = "Get a book by ID",
            description = "Retrieves one book using its unique identifier."
    )
    @GetMapping("/{id}")
    public BookResponse getBookById(
            @PathVariable Long id
    ) {
        return bookService.getBookById(id);
    }

    @Operation(
            summary = "Get all books",
            description = """
                    Retrieves a paginated list of books with optional
                    search, category, and status filtering.
                    """
    )
    @GetMapping
    public BookPageResponse getAllBooks(

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            BookCategory category,

            @RequestParam(required = false)
            BookStatus status,

            @PageableDefault(
                    size = 12,
                    sort = "id",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {
        return bookService.getAllBooks(
                search,
                category,
                status,
                pageable
        );
    }

    @Operation(
            summary = "Update a book",
            description = "Updates the details of an existing book."
    )
    @PutMapping("/{id}")
    public BookResponse updateBook(
            @PathVariable Long id,
            @Valid
            @RequestBody BookRequest request
    ) {
        return bookService.updateBook(id, request);
    }

    @Operation(
            summary = "Delete a book",
            description = "Deletes an existing book using its ID."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(
            @PathVariable Long id
    ) {
        bookService.deleteBook(id);
    }
}