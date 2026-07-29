package com.aman.LibraryManagementSystem.exception.member;

public class DuplicateMemberException extends RuntimeException{

    public DuplicateMemberException(String email){
        super("Member with '" + email + "' already exists.");
    }
}
