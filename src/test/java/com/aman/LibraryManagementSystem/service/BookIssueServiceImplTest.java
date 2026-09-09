package com.aman.LibraryManagementSystem.service;

import com.aman.LibraryManagementSystem.dto.request.BookIssueRequest;
import com.aman.LibraryManagementSystem.dto.response.BookIssueResponse;
import com.aman.LibraryManagementSystem.entity.Book;
import com.aman.LibraryManagementSystem.entity.BookIssue;
import com.aman.LibraryManagementSystem.entity.Member;
import com.aman.LibraryManagementSystem.enums.BookStatus;
import com.aman.LibraryManagementSystem.enums.IssueStatus;
import com.aman.LibraryManagementSystem.exception.book.BookNotFoundException;
import com.aman.LibraryManagementSystem.exception.issue.BookAlreadyIssuedException;
import com.aman.LibraryManagementSystem.exception.issue.BookAlreadyReturnedException;
import com.aman.LibraryManagementSystem.exception.issue.BookIssueNotFoundException;
import com.aman.LibraryManagementSystem.exception.member.MemberNotFoundException;
import com.aman.LibraryManagementSystem.mapper.BookIssueMapper;
import com.aman.LibraryManagementSystem.repository.BookIssueRepository;
import com.aman.LibraryManagementSystem.repository.BookRepository;
import com.aman.LibraryManagementSystem.repository.MemberRepository;
import com.aman.LibraryManagementSystem.service.impl.BookIssueServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookIssueServiceImplTest {

    @Mock
    private BookIssueRepository bookIssueRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookIssueMapper bookIssueMapper;

    @InjectMocks
    private BookIssueServiceImpl bookIssueService;

    // ============================================================
    // ISSUE BOOK TESTS
    // ============================================================

    @Test
    void shouldIssueBookSuccessfully() {

        // Arrange
        Long bookId = 101L;
        Long memberId = 1L;

        BookIssueRequest request = new BookIssueRequest(
                bookId,
                memberId
        );

        Book book = mock(Book.class);
        Member member = mock(Member.class);
        BookIssue bookIssue = mock(BookIssue.class);
        BookIssueResponse response = mock(BookIssueResponse.class);

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(member));

        when(book.getStatus())
                .thenReturn(BookStatus.AVAILABLE);

        when(
                bookIssueMapper.toEntity(
                        eq(request),
                        eq(book),
                        eq(member),
                        any(LocalDate.class),
                        any(LocalDate.class),
                        eq(IssueStatus.ISSUED)
                )
        ).thenReturn(bookIssue);

        when(bookIssueRepository.save(bookIssue))
                .thenReturn(bookIssue);

        when(bookIssueMapper.toResponse(bookIssue))
                .thenReturn(response);

        // Act
        BookIssueResponse result =
                bookIssueService.issueBook(request);

        // Assert
        assertSame(response, result);

        verify(bookRepository)
                .findById(bookId);

        verify(memberRepository)
                .findById(memberId);

        verify(book)
                .setStatus(BookStatus.ISSUED);

        verify(bookIssueMapper)
                .toEntity(
                        eq(request),
                        eq(book),
                        eq(member),
                        any(LocalDate.class),
                        any(LocalDate.class),
                        eq(IssueStatus.ISSUED)
                );

        verify(bookIssueRepository)
                .save(bookIssue);

        verify(bookIssueMapper)
                .toResponse(bookIssue);
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenBookDoesNotExist() {

        // Arrange
        Long bookId = 999L;
        Long memberId = 1L;

        BookIssueRequest request = new BookIssueRequest(
                bookId,
                memberId
        );

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                BookNotFoundException.class,
                () -> bookIssueService.issueBook(request)
        );

        verify(bookRepository)
                .findById(bookId);

        verifyNoInteractions(
                memberRepository,
                bookIssueRepository,
                bookIssueMapper
        );
    }

    @Test
    void shouldThrowMemberNotFoundExceptionWhenMemberDoesNotExist() {

        // Arrange
        Long bookId = 101L;
        Long memberId = 999L;

        BookIssueRequest request = new BookIssueRequest(
                bookId,
                memberId
        );

        Book book = mock(Book.class);

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                MemberNotFoundException.class,
                () -> bookIssueService.issueBook(request)
        );

        verify(bookRepository)
                .findById(bookId);

        verify(memberRepository)
                .findById(memberId);

        verifyNoInteractions(
                bookIssueRepository,
                bookIssueMapper
        );
    }

    @Test
    void shouldThrowBookAlreadyIssuedExceptionWhenBookIsNotAvailable() {

        Long bookId = 101L;
        Long memberId = 1L;

        BookIssueRequest request = new BookIssueRequest(
                bookId,
                memberId
        );

        Book book = mock(Book.class);
        Member member = mock(Member.class);

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(member));

        when(book.getStatus())
                .thenReturn(BookStatus.ISSUED);

        assertThrows(
                BookAlreadyIssuedException.class,
                () -> bookIssueService.issueBook(request)
        );

        verify(bookRepository)
                .findById(bookId);

        verify(memberRepository)
                .findById(memberId);

        verify(book, times(2))
                .getStatus();

        verifyNoInteractions(
                bookIssueRepository,
                bookIssueMapper
        );
    }

    // ============================================================
    // RETURN BOOK TESTS
    // ============================================================

    @Test
    void shouldReturnBookSuccessfully() {

        // Arrange
        Long issueId = 1L;

        BookIssue bookIssue = mock(BookIssue.class);
        Book book = mock(Book.class);
        BookIssueResponse response = mock(BookIssueResponse.class);

        when(bookIssueRepository.findById(issueId))
                .thenReturn(Optional.of(bookIssue));

        when(bookIssue.getIssueStatus())
                .thenReturn(IssueStatus.ISSUED);

        when(bookIssue.getBook())
                .thenReturn(book);

        when(bookIssueRepository.save(bookIssue))
                .thenReturn(bookIssue);

        when(bookIssueMapper.toResponse(bookIssue))
                .thenReturn(response);

        // Act
        BookIssueResponse result =
                bookIssueService.returnBook(issueId);

        // Assert
        assertSame(response, result);

        verify(bookIssueRepository)
                .findById(issueId);

        verify(bookIssue)
                .getIssueStatus();

        verify(bookIssue)
                .setReturnDate(any(LocalDate.class));

        verify(bookIssue)
                .setIssueStatus(IssueStatus.RETURNED);

        verify(bookIssue)
                .getBook();

        verify(book)
                .setStatus(BookStatus.AVAILABLE);

        verify(bookIssueRepository)
                .save(bookIssue);

        verify(bookIssueMapper)
                .toResponse(bookIssue);
    }

    @Test
    void shouldThrowBookIssueNotFoundExceptionWhenReturningNonExistingIssue() {

        // Arrange
        Long issueId = 999L;

        when(bookIssueRepository.findById(issueId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                BookIssueNotFoundException.class,
                () -> bookIssueService.returnBook(issueId)
        );

        verify(bookIssueRepository)
                .findById(issueId);

        verifyNoInteractions(
                bookIssueMapper
        );
    }

    @Test
    void shouldThrowBookIssueNotFoundExceptionWhenIssueDoesNotExist() {

        // Arrange
        Long issueId = 999L;

        when(bookIssueRepository.findById(issueId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                BookIssueNotFoundException.class,
                () -> bookIssueService.getBookIssueById(issueId)
        );

        verify(bookIssueRepository)
                .findById(issueId);

        verifyNoInteractions(
                bookIssueMapper
        );
    }

    @Test
    void shouldThrowBookAlreadyReturnedExceptionWhenIssueIsAlreadyReturned() {

        // Arrange
        Long issueId = 1L;

        BookIssue bookIssue = mock(BookIssue.class);

        when(bookIssueRepository.findById(issueId))
                .thenReturn(Optional.of(bookIssue));

        when(bookIssue.getIssueStatus())
                .thenReturn(IssueStatus.RETURNED);

        // Act & Assert
        assertThrows(
                BookAlreadyReturnedException.class,
                () -> bookIssueService.returnBook(issueId)
        );

        verify(bookIssueRepository)
                .findById(issueId);

        verify(bookIssue)
                .getIssueStatus();

        verifyNoMoreInteractions(bookIssue);

        verifyNoInteractions(
                bookIssueMapper
        );
    }

    // ============================================================
    // HISTORY TESTS
    // ============================================================

    @Test
    void shouldGetAllBookIssues() {

        // Arrange
        BookIssue issue1 = mock(BookIssue.class);
        BookIssue issue2 = mock(BookIssue.class);

        BookIssueResponse response1 = mock(BookIssueResponse.class);
        BookIssueResponse response2 = mock(BookIssueResponse.class);

        when(bookIssueRepository.findAll())
                .thenReturn(List.of(issue1, issue2));

        when(bookIssueMapper.toResponse(issue1))
                .thenReturn(response1);

        when(bookIssueMapper.toResponse(issue2))
                .thenReturn(response2);

        // Act
        List<BookIssueResponse> result =
                bookIssueService.getAllBookIssues();

        // Assert
        assertEquals(2, result.size());
        assertSame(response1, result.get(0));
        assertSame(response2, result.get(1));

        verify(bookIssueRepository)
                .findAll();

        verify(bookIssueMapper)
                .toResponse(issue1);

        verify(bookIssueMapper)
                .toResponse(issue2);
    }

    @Test
    void shouldGetBookIssueByIdSuccessfully() {

        // Arrange
        Long issueId = 1L;

        BookIssue bookIssue = mock(BookIssue.class);
        BookIssueResponse response = mock(BookIssueResponse.class);

        when(bookIssueRepository.findById(issueId))
                .thenReturn(Optional.of(bookIssue));

        when(bookIssueMapper.toResponse(bookIssue))
                .thenReturn(response);

        // Act
        BookIssueResponse result =
                bookIssueService.getBookIssueById(issueId);

        // Assert
        assertSame(response, result);

        verify(bookIssueRepository)
                .findById(issueId);

        verify(bookIssueMapper)
                .toResponse(bookIssue);
    }

    @Test
    void shouldGetBookIssueHistoryByMemberSuccessfully() {

        // Arrange
        Long memberId = 1L;

        Member member = mock(Member.class);
        BookIssue issue1 = mock(BookIssue.class);
        BookIssue issue2 = mock(BookIssue.class);

        BookIssueResponse response1 = mock(BookIssueResponse.class);
        BookIssueResponse response2 = mock(BookIssueResponse.class);

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(member));

        when(
                bookIssueRepository
                        .findByMemberIdOrderByIssueDateDesc(memberId)
        ).thenReturn(List.of(issue1, issue2));

        when(bookIssueMapper.toResponse(issue1))
                .thenReturn(response1);

        when(bookIssueMapper.toResponse(issue2))
                .thenReturn(response2);

        // Act
        List<BookIssueResponse> result =
                bookIssueService.getBookIssueHistoryByMember(memberId);

        // Assert
        assertEquals(2, result.size());
        assertSame(response1, result.get(0));
        assertSame(response2, result.get(1));

        verify(memberRepository)
                .findById(memberId);

        verify(bookIssueRepository)
                .findByMemberIdOrderByIssueDateDesc(memberId);

        verify(bookIssueMapper)
                .toResponse(issue1);

        verify(bookIssueMapper)
                .toResponse(issue2);
    }

    @Test
    void shouldGetBookIssueHistoryByBookSuccessfully() {

        // Arrange
        Long bookId = 101L;

        Book book = mock(Book.class);
        BookIssue issue1 = mock(BookIssue.class);
        BookIssue issue2 = mock(BookIssue.class);

        BookIssueResponse response1 = mock(BookIssueResponse.class);
        BookIssueResponse response2 = mock(BookIssueResponse.class);

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(
                bookIssueRepository
                        .findByBookIdOrderByIssueDateDesc(bookId)
        ).thenReturn(List.of(issue1, issue2));

        when(bookIssueMapper.toResponse(issue1))
                .thenReturn(response1);

        when(bookIssueMapper.toResponse(issue2))
                .thenReturn(response2);

        // Act
        List<BookIssueResponse> result =
                bookIssueService.getBookIssueHistoryByBook(bookId);

        // Assert
        assertEquals(2, result.size());
        assertSame(response1, result.get(0));
        assertSame(response2, result.get(1));

        verify(bookRepository)
                .findById(bookId);

        verify(bookIssueRepository)
                .findByBookIdOrderByIssueDateDesc(bookId);

        verify(bookIssueMapper)
                .toResponse(issue1);

        verify(bookIssueMapper)
                .toResponse(issue2);
    }

    @Test
    void shouldThrowMemberNotFoundExceptionWhenGettingBookIssueHistory() {

        // Arrange
        Long memberId = 999L;

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                MemberNotFoundException.class,
                () -> bookIssueService.getBookIssueHistoryByMember(memberId)
        );

        verify(memberRepository)
                .findById(memberId);

        verifyNoInteractions(
                bookIssueRepository,
                bookIssueMapper
        );
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenGettingBookIssueHistory() {

        // Arrange
        Long bookId = 999L;

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                BookNotFoundException.class,
                () -> bookIssueService.getBookIssueHistoryByBook(bookId)
        );

        verify(bookRepository)
                .findById(bookId);

        verifyNoInteractions(
                bookIssueRepository,
                bookIssueMapper
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoBookIssuesExist() {

        // Arrange
        when(bookIssueRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<BookIssueResponse> result =
                bookIssueService.getAllBookIssues();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookIssueRepository)
                .findAll();

        verifyNoInteractions(
                bookIssueMapper
        );
    }

    @Test
    void shouldReturnEmptyListWhenMemberHasNoIssueHistory() {

        // Arrange
        Long memberId = 999L;

        Member member = mock(Member.class);

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(member));

        when(
                bookIssueRepository
                        .findByMemberIdOrderByIssueDateDesc(memberId)
        ).thenReturn(List.of());

        // Act
        List<BookIssueResponse> result =
                bookIssueService.getBookIssueHistoryByMember(memberId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(memberRepository)
                .findById(memberId);

        verify(bookIssueRepository)
                .findByMemberIdOrderByIssueDateDesc(memberId);

        verifyNoInteractions(
                bookIssueMapper
        );
    }

    @Test
    void shouldReturnEmptyListWhenBookHasNoIssueHistory() {

        // Arrange
        Long bookId = 999L;

        Book book = mock(Book.class);

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(
                bookIssueRepository
                        .findByBookIdOrderByIssueDateDesc(bookId)
        ).thenReturn(List.of());

        // Act
        List<BookIssueResponse> result =
                bookIssueService.getBookIssueHistoryByBook(bookId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookRepository)
                .findById(bookId);

        verify(bookIssueRepository)
                .findByBookIdOrderByIssueDateDesc(bookId);

        verifyNoInteractions(
                bookIssueMapper
        );
    }

    @Test
    void shouldThrowMemberNotFoundExceptionWhenGettingBookIssueHistoryForNonExistingMember() {

        // Arrange
        Long memberId = 999L;

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                MemberNotFoundException.class,
                () -> bookIssueService.getBookIssueHistoryByMember(memberId)
        );

        verify(memberRepository)
                .findById(memberId);

        verifyNoInteractions(
                bookIssueRepository,
                bookIssueMapper
        );
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenGettingBookIssueHistoryForNonExistingBook() {

        // Arrange
        Long bookId = 999L;

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                BookNotFoundException.class,
                () -> bookIssueService.getBookIssueHistoryByBook(bookId)
        );

        verify(bookRepository)
                .findById(bookId);

        verifyNoInteractions(
                bookIssueRepository,
                bookIssueMapper
        );
    }

    @Test
    void shouldSetDueDateFourteenDaysAfterIssueDate() {

        // Arrange
        Long bookId = 101L;
        Long memberId = 1L;

        BookIssueRequest request = new BookIssueRequest(
                bookId,
                memberId
        );

        Book book = mock(Book.class);
        Member member = mock(Member.class);
        BookIssue bookIssue = mock(BookIssue.class);
        BookIssueResponse response = mock(BookIssueResponse.class);

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(member));

        when(book.getStatus())
                .thenReturn(BookStatus.AVAILABLE);

        when(
                bookIssueMapper.toEntity(
                        eq(request),
                        eq(book),
                        eq(member),
                        any(LocalDate.class),
                        any(LocalDate.class),
                        eq(IssueStatus.ISSUED)
                )
        ).thenReturn(bookIssue);

        when(bookIssueRepository.save(bookIssue))
                .thenReturn(bookIssue);

        when(bookIssueMapper.toResponse(bookIssue))
                .thenReturn(response);

        // Act
        bookIssueService.issueBook(request);

        // Assert
        verify(bookIssueMapper).toEntity(
                eq(request),
                eq(book),
                eq(member),
                eq(LocalDate.now()),
                eq(LocalDate.now().plusDays(14)),
                eq(IssueStatus.ISSUED)
        );
    }

    @Test
    void shouldSetReturnDateToTodayWhenBookIsReturned() {

        // Arrange
        Long issueId = 1L;

        BookIssue bookIssue = mock(BookIssue.class);
        Book book = mock(Book.class);

        when(bookIssueRepository.findById(issueId))
                .thenReturn(Optional.of(bookIssue));

        when(bookIssue.getIssueStatus())
                .thenReturn(IssueStatus.ISSUED);

        when(bookIssue.getBook())
                .thenReturn(book);

        when(bookIssueRepository.save(bookIssue))
                .thenReturn(bookIssue);

        when(bookIssueMapper.toResponse(bookIssue))
                .thenReturn(mock(BookIssueResponse.class));

        // Act
        bookIssueService.returnBook(issueId);

        // Assert
        verify(bookIssue)
                .setReturnDate(LocalDate.now());

        verify(bookIssue)
                .setIssueStatus(IssueStatus.RETURNED);

        verify(book)
                .setStatus(BookStatus.AVAILABLE);

        verify(bookIssueRepository)
                .save(bookIssue);
    }
}