package com.aman.LibraryManagementSystem.controller;

import com.aman.LibraryManagementSystem.dto.request.BookIssueRequest;
import com.aman.LibraryManagementSystem.dto.response.BookIssueResponse;
import com.aman.LibraryManagementSystem.service.BookIssueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;


@Tag(
        name = "Book Issues",
        description = "Endpoints for issuing, returning, and tracking books"
)
@RestController
@RequestMapping("/book-issues")
public class BookIssueController {

    private final BookIssueService bookIssueService;

    public BookIssueController(BookIssueService bookIssueService){
        this.bookIssueService = bookIssueService;
    }

    @Operation(
            summary = "Issue a book",
            description = "Issues an available book to a library member."
    )
    @PostMapping
    public ResponseEntity<BookIssueResponse> issueBook(
            @Valid
            @RequestBody
            BookIssueRequest request
    ){
        BookIssueResponse response = bookIssueService.issueBook(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Return a book",
            description = "Marks a previously issued book as returned."
    )
    @PutMapping("/{issueId}/return")
    public ResponseEntity<BookIssueResponse> returnBook(
            @PathVariable Long issueId
    ){
        BookIssueResponse response = bookIssueService.returnBook(issueId);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get all book issues",
            description = "Retrieves every book issue record."
    )
    @GetMapping
    public ResponseEntity<List<BookIssueResponse>> getAllBookIssues(){
        List<BookIssueResponse> response = bookIssueService.getAllBookIssues();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get book issue by ID",
            description = "Retrieves one book issue record using its ID."
    )
    @GetMapping("/{issueId}")
    public ResponseEntity<BookIssueResponse> getBookIssueById(
            @PathVariable Long issueId
    ){
        BookIssueResponse response = bookIssueService.getBookIssueById(issueId);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get member issue history",
            description = "Retrieves all book issues belonging to one member, newest first."
    )
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BookIssueResponse>> getBookIssueHistoryByMember(
            @PathVariable Long memberId
    ){
        List<BookIssueResponse> response = bookIssueService.getBookIssueHistoryByMember(memberId);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get book issue history",
            description = "Retrieves all issue records for one book, newest first."
    )
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BookIssueResponse>> getBookIssueHistoryByBook(
            @PathVariable Long bookId
    ){
        List<BookIssueResponse> response = bookIssueService.getBookIssueHistoryByBook(bookId);

        return ResponseEntity.ok(response);
    }
}
