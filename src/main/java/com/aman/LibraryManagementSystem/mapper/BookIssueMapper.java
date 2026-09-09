package com.aman.LibraryManagementSystem.mapper;

import com.aman.LibraryManagementSystem.dto.request.BookIssueRequest;
import com.aman.LibraryManagementSystem.dto.response.BookIssueResponse;
import com.aman.LibraryManagementSystem.entity.Book;
import com.aman.LibraryManagementSystem.entity.BookIssue;
import com.aman.LibraryManagementSystem.entity.Member;
import com.aman.LibraryManagementSystem.enums.IssueStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BookIssueMapper {

    public BookIssue toEntity(
            BookIssueRequest request,
            Book book,
            Member member,
            LocalDate issueDate,
            LocalDate dueDate,
            IssueStatus issueStatus
    ){
        return new BookIssue(book,member,issueDate,dueDate,null,issueStatus);
    }

    public BookIssueResponse toResponse(BookIssue bookIssue){
        return new BookIssueResponse(
                bookIssue.getId(),
                bookIssue.getBook().getId(),
                bookIssue.getMember().getId(),
                bookIssue.getIssueDate(),
                bookIssue.getDueDate(),
                bookIssue.getReturnDate(),
                bookIssue.getIssueStatus()
        );
    }
}
