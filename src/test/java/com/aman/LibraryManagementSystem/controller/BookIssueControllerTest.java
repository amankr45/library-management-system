package com.aman.LibraryManagementSystem.controller;

import com.aman.LibraryManagementSystem.dto.request.BookIssueRequest;
import com.aman.LibraryManagementSystem.dto.response.BookIssueResponse;
import com.aman.LibraryManagementSystem.enums.IssueStatus;
import com.aman.LibraryManagementSystem.exception.book.BookNotFoundException;
import com.aman.LibraryManagementSystem.exception.issue.BookAlreadyIssuedException;
import com.aman.LibraryManagementSystem.exception.issue.BookAlreadyReturnedException;
import com.aman.LibraryManagementSystem.exception.issue.BookIssueNotFoundException;
import com.aman.LibraryManagementSystem.exception.member.MemberNotFoundException;
import com.aman.LibraryManagementSystem.service.BookIssueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.aman.LibraryManagementSystem.config.SecurityConfig;
import com.aman.LibraryManagementSystem.security.CustomUserDetailsService;
import org.springframework.context.annotation.Import;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookIssueController.class)
@Import(SecurityConfig.class)
class BookIssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookIssueService bookIssueService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    // ============================================================
    // TEST DATA
    // ============================================================

    private BookIssueResponse createIssuedBookIssueResponse() {

        return new BookIssueResponse(
                1L,
                101L,
                1L,
                LocalDate.of(2026, 8, 13),
                LocalDate.of(2026, 8, 27),
                null,
                IssueStatus.ISSUED
        );
    }

    // ============================================================
    // ISSUE BOOK
    // ============================================================

    @Test
    void shouldIssueBookSuccessfully() throws Exception {

        BookIssueResponse response =
                createIssuedBookIssueResponse();

        when(bookIssueService.issueBook(any(BookIssueRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/book-issues")
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "bookId": 101,
                            "memberId": 1
                        }
                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookId").value(101))
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.issueDate").value("2026-08-13"))
                .andExpect(jsonPath("$.dueDate").value("2026-08-27"))
                .andExpect(jsonPath("$.returnDate").doesNotExist())
                .andExpect(jsonPath("$.issueStatus").value("ISSUED"));

        verify(bookIssueService)
                .issueBook(any(BookIssueRequest.class));
    }

    // ============================================================
    // RETURN BOOK
    // ============================================================

    @Test
    void shouldReturnBookSuccessfully() throws Exception {

        Long issueId = 1L;

        BookIssueResponse response =
                new BookIssueResponse(
                        1L,
                        101L,
                        1L,
                        LocalDate.of(2026, 8, 13),
                        LocalDate.of(2026, 8, 27),
                        LocalDate.of(2026, 8, 13),
                        IssueStatus.RETURNED
                );

        when(bookIssueService.returnBook(issueId))
                .thenReturn(response);

        mockMvc.perform(
                        put("/book-issues/{issueId}/return", issueId)
                                .with(jwt())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookId").value(101))
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.issueStatus").value("RETURNED"));

        verify(bookIssueService)
                .returnBook(issueId);
    }

    // ============================================================
    // GET ALL ISSUES
    // ============================================================

    @Test
    void shouldGetAllBookIssuesSuccessfully() throws Exception {

        BookIssueResponse response =
                createIssuedBookIssueResponse();

        when(bookIssueService.getAllBookIssues())
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/book-issues")
                                .with(jwt())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].bookId").value(101))
                .andExpect(jsonPath("$[0].memberId").value(1))
                .andExpect(jsonPath("$[0].issueStatus").value("ISSUED"));

        verify(bookIssueService)
                .getAllBookIssues();
    }

    // ============================================================
    // GET ISSUE BY ID
    // ============================================================

    @Test
    void shouldGetBookIssueByIdSuccessfully() throws Exception {

        Long issueId = 1L;

        BookIssueResponse response =
                createIssuedBookIssueResponse();

        when(bookIssueService.getBookIssueById(issueId))
                .thenReturn(response);

        mockMvc.perform(
                        get("/book-issues/{issueId}", issueId)
                                .with(jwt())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookId").value(101))
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.issueStatus").value("ISSUED"));

        verify(bookIssueService)
                .getBookIssueById(issueId);
    }

    // ============================================================
    // MEMBER HISTORY
    // ============================================================

    @Test
    void shouldGetBookIssueHistoryByMemberSuccessfully()
            throws Exception {

        Long memberId = 1L;

        BookIssueResponse response =
                createIssuedBookIssueResponse();

        when(bookIssueService.getBookIssueHistoryByMember(memberId))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/book-issues/member/{memberId}", memberId)
                                .with(jwt())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].memberId").value(1))
                .andExpect(jsonPath("$[0].bookId").value(101));

        verify(bookIssueService)
                .getBookIssueHistoryByMember(memberId);
    }

    // ============================================================
    // BOOK HISTORY
    // ============================================================

    @Test
    void shouldGetBookIssueHistoryByBookSuccessfully()
            throws Exception {

        Long bookId = 101L;

        BookIssueResponse response =
                createIssuedBookIssueResponse();

        when(bookIssueService.getBookIssueHistoryByBook(bookId))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/book-issues/book/{bookId}", bookId)
                                .with(jwt())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(101))
                .andExpect(jsonPath("$[0].memberId").value(1));

        verify(bookIssueService)
                .getBookIssueHistoryByBook(bookId);
    }

    // ============================================================
    // VALIDATION
    // ============================================================

    @Test
    void shouldReturnBadRequestWhenBookIdIsMissing()
            throws Exception {

        mockMvc.perform(
                        post("/book-issues")
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "memberId": 1
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenMemberIdIsMissing()
            throws Exception {

        mockMvc.perform(
                        post("/book-issues")
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "bookId": 101
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());
    }

    // ============================================================
    // EXCEPTION PROPAGATION
    // ============================================================

    @Test
    void shouldPropagateBookNotFoundException()
            throws Exception {

        when(bookIssueService.issueBook(any(BookIssueRequest.class)))
                .thenThrow(
                        new BookNotFoundException(999L)
                );

        mockMvc.perform(
                        post("/book-issues")
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "bookId": 999,
                                            "memberId": 1
                                        }
                                        """)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPropagateMemberNotFoundException()
            throws Exception {

        when(bookIssueService.issueBook(any(BookIssueRequest.class)))
                .thenThrow(
                        new MemberNotFoundException(999L)
                );

        mockMvc.perform(
                        post("/book-issues")
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "bookId": 101,
                                            "memberId": 999
                                        }
                                        """)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPropagateBookAlreadyIssuedException()
            throws Exception {

        when(bookIssueService.issueBook(any(BookIssueRequest.class)))
                .thenThrow(
                        new BookAlreadyIssuedException(
                                "Book is already issued"
                        )
                );

        mockMvc.perform(
                        post("/book-issues")
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "bookId": 101,
                                            "memberId": 1
                                        }
                                        """)
                )
                .andExpect(status().isConflict());
    }

    @Test
    void shouldPropagateBookIssueNotFoundException()
            throws Exception {

        Long issueId = 999L;

        when(bookIssueService.returnBook(issueId))
                .thenThrow(
                        new BookIssueNotFoundException(issueId)
                );

        mockMvc.perform(
                        put("/book-issues/{issueId}/return", issueId)
                                .with(jwt())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPropagateBookAlreadyReturnedException()
            throws Exception {

        Long issueId = 1L;

        when(bookIssueService.returnBook(issueId))
                .thenThrow(
                        new BookAlreadyReturnedException(
                                "Book issue has already been returned"
                        )
                );

        mockMvc.perform(
                        put("/book-issues/{issueId}/return", issueId)
                                .with(jwt())
                )
                .andExpect(status().isConflict());
    }
}