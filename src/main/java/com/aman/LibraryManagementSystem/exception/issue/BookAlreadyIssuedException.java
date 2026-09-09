package com.aman.LibraryManagementSystem.exception.issue;

public class BookAlreadyIssuedException extends RuntimeException{

    public BookAlreadyIssuedException(String message){
        super(message);
    }
}
