package com.aman.LibraryManagementSystem.dto.response;

import com.aman.LibraryManagementSystem.enums.IssueStatus;

import java.time.LocalDate;

public class BookIssueResponse {
    private Long id;
    private Long bookId;
    private Long memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private IssueStatus issueStatus;

    public BookIssueResponse(
        Long id,
        Long bookId,
        Long memberId,
        LocalDate issueDate,
        LocalDate dueDate,
        LocalDate returnDate,
        IssueStatus issueStatus
    ){
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.issueStatus = issueStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public IssueStatus getIssueStatus() {
        return issueStatus;
    }
}
