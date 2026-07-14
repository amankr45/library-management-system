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

    // getters

    // setters

}