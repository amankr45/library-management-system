package com.aman.LibraryManagementSystem.exception.book;

public class DuplicateBookException
        extends RuntimeException {

    public DuplicateBookException(
            String message
    ) {
        super(message);
    }

}