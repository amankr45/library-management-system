package com.aman.LibraryManagementSystem.service;

import com.aman.LibraryManagementSystem.dto.request.BookIssueRequest;
import com.aman.LibraryManagementSystem.dto.response.BookIssueResponse;

import java.util.List;

public interface BookIssueService {
    BookIssueResponse issueBook(BookIssueRequest request);

    BookIssueResponse returnBook(Long issueId);

    List<BookIssueResponse> getAllBookIssues();

    BookIssueResponse getBookIssueById(Long issueId);

    List<BookIssueResponse> getBookIssueHistoryByMember(Long memberId);

    List<BookIssueResponse> getBookIssueHistoryByBook(Long bookId);
}
