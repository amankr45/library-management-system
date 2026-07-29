package com.aman.LibraryManagementSystem.exception.member;

public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException(Long id){
        super("Member not found with id : " + id);
    }
}
