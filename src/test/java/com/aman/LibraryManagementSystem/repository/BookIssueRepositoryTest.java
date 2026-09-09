package com.aman.LibraryManagementSystem.repository;

import com.aman.LibraryManagementSystem.entity.Book;
import com.aman.LibraryManagementSystem.entity.BookIssue;
import com.aman.LibraryManagementSystem.entity.Member;
import com.aman.LibraryManagementSystem.enums.BookCategory;
import com.aman.LibraryManagementSystem.enums.IssueStatus;
import com.aman.LibraryManagementSystem.enums.MembershipType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookIssueRepositoryTest {

    @Autowired
    private BookIssueRepository bookIssueRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void shouldFindBookIssuesByBookIdOrderedByIssueDateDescending() {

        Book firstBook = bookRepository.save(
                new Book(
                        "Clean Code",
                        "Robert C. Martin",
                        "ISSUE-BOOK-001",
                        BookCategory.PROGRAMMING
                )
        );

        Book secondBook = bookRepository.save(
                new Book(
                        "The Hobbit",
                        "J.R.R. Tolkien",
                        "ISSUE-BOOK-002",
                        BookCategory.FICTION
                )
        );

        Member member = memberRepository.save(
                new Member(
                        "Aman Kumar",
                        "aman-issue-001@test.com",
                        "9876543210",
                        MembershipType.STUDENT
                )
        );

        BookIssue olderIssue = bookIssueRepository.save(
                new BookIssue(
                        firstBook,
                        member,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 15),
                        null,
                        IssueStatus.ISSUED
                )
        );

        BookIssue newerIssue = bookIssueRepository.save(
                new BookIssue(
                        firstBook,
                        member,
                        LocalDate.of(2026, 8, 10),
                        LocalDate.of(2026, 8, 24),
                        null,
                        IssueStatus.ISSUED
                )
        );

        bookIssueRepository.save(
                new BookIssue(
                        secondBook,
                        member,
                        LocalDate.of(2026, 8, 12),
                        LocalDate.of(2026, 8, 26),
                        null,
                        IssueStatus.ISSUED
                )
        );

        List<BookIssue> issues =
                bookIssueRepository
                        .findByBookIdOrderByIssueDateDesc(firstBook.getId());

        assertThat(issues)
                .hasSize(2)
                .extracting(BookIssue::getId)
                .containsExactly(newerIssue.getId(), olderIssue.getId());
    }

    @Test
    void shouldFindBookIssuesByMemberIdOrderedByIssueDateDescending() {

        Book book = bookRepository.save(
                new Book(
                        "Effective Java",
                        "Joshua Bloch",
                        "ISSUE-BOOK-003",
                        BookCategory.PROGRAMMING
                )
        );

        Member firstMember = memberRepository.save(
                new Member(
                        "First Member",
                        "first-member-issue@test.com",
                        "9876543211",
                        MembershipType.STUDENT
                )
        );

        Member secondMember = memberRepository.save(
                new Member(
                        "Second Member",
                        "second-member-issue@test.com",
                        "9876543212",
                        MembershipType.FACULTY
                )
        );

        BookIssue olderIssue = bookIssueRepository.save(
                new BookIssue(
                        book,
                        firstMember,
                        LocalDate.of(2026, 8, 2),
                        LocalDate.of(2026, 8, 16),
                        null,
                        IssueStatus.ISSUED
                )
        );

        BookIssue newerIssue = bookIssueRepository.save(
                new BookIssue(
                        book,
                        firstMember,
                        LocalDate.of(2026, 8, 11),
                        LocalDate.of(2026, 8, 25),
                        null,
                        IssueStatus.ISSUED
                )
        );

        bookIssueRepository.save(
                new BookIssue(
                        book,
                        secondMember,
                        LocalDate.of(2026, 8, 13),
                        LocalDate.of(2026, 8, 27),
                        null,
                        IssueStatus.ISSUED
                )
        );

        List<BookIssue> issues =
                bookIssueRepository
                        .findByMemberIdOrderByIssueDateDesc(firstMember.getId());

        assertThat(issues)
                .hasSize(2)
                .extracting(BookIssue::getId)
                .containsExactly(newerIssue.getId(), olderIssue.getId());
    }

    @Test
    void shouldReturnEmptyListWhenBookHasNoIssues() {

        Book book = bookRepository.save(
                new Book(
                        "No Issues Yet",
                        "Test Author",
                        "ISSUE-BOOK-004",
                        BookCategory.SCIENCE
                )
        );

        List<BookIssue> issues =
                bookIssueRepository
                        .findByBookIdOrderByIssueDateDesc(book.getId());

        assertThat(issues)
                .isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenMemberHasNoIssues() {

        Member member = memberRepository.save(
                new Member(
                        "No Issue Member",
                        "no-issue-member@test.com",
                        "9876543213",
                        MembershipType.STAFF
                )
        );

        List<BookIssue> issues =
                bookIssueRepository
                        .findByMemberIdOrderByIssueDateDesc(member.getId());

        assertThat(issues)
                .isEmpty();
    }
}