package com.aman.LibraryManagementSystem.dto.request;

import jakarta.validation.constraints.NotNull;

public class BookIssueRequest {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Member ID is required")
    private Long memberId;

    public BookIssueRequest(){
    }

    public BookIssueRequest(Long bookId,Long memberId){
        this.bookId = bookId;
        this.memberId = memberId;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
