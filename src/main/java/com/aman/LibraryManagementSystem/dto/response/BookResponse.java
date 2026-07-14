package com.aman.LibraryManagementSystem.dto.response;

import com.aman.LibraryManagementSystem.enums.BookCategory;
import com.aman.LibraryManagementSystem.enums.BookStatus;

public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BookCategory category;
    private BookStatus status;

    public BookResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(
            Long id
    ) {
        this.id = id;
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

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(
            BookStatus status
    ) {
        this.status = status;
    }
}