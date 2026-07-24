package com.aman.LibraryManagementSystem.dto.request;

import com.aman.LibraryManagementSystem.enums.BookCategory;
import com.aman.LibraryManagementSystem.validation.ValidIsbn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BookRequest {

    @NotBlank(message = "Title is required")
    @Size(
            min = 2,
            max = 200,
            message = "Title must be between 2 and 200 characters"
    )
    private String title;

    @NotBlank(message = "Author is required")
    @Size(
            min = 2,
            max = 100,
            message = "Author name must be between 2 and 100 characters"
    )
    @Pattern(
            regexp = "^[A-Za-z .'-]+$",
            message = "Author name contains invalid characters"
    )
    private String author;

    @NotBlank(message = "ISBN is required")
    @ValidIsbn
    private String isbn;

    @NotNull(message = "Category is required.")
    private BookCategory category;

    public BookRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(
            String title
    ) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(
            String author
    ) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(
            String isbn
    ) {
        this.isbn = isbn;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(
            BookCategory category
    ) {
        this.category = category;
    }
}