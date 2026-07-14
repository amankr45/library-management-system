package com.aman.LibraryManagementSystem.dto.request;

import com.aman.LibraryManagementSystem.enums.BookCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BookRequest {

    @NotBlank(message = "Title is required.")
    @Size(min = 2,max = 200, message = "Title must not exceed 200 characters.")
    private String title;

    @NotBlank(message = "Author is required.")
    @Size(max = 100, message = "Author must not exceed 100 characters.")
    private String author;

    @Pattern(
            regexp = "^[0-9-]+$"
    )
    @NotBlank(message = "ISBN is required.")
    @Size(max = 20, message = "ISBN must not exceed 20 characters.")
    private String isbn;

    @NotNull(message = "Category is required.")
    private BookCategory category;

    // constructors
    // getters
    // setters
}