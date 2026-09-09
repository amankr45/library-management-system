package com.aman.LibraryManagementSystem.exception.issue;

public class BookIssueNotFoundException extends RuntimeException{

    public BookIssueNotFoundException(Long id){
        super("Book issue not found with id : " + id);
    }
}
