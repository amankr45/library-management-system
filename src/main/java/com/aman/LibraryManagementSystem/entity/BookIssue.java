package com.aman.LibraryManagementSystem.entity;

import com.aman.LibraryManagementSystem.enums.IssueStatus;
import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "book_issues")
public class BookIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "bookIssueSeqGen",
            sequenceName = "book_issue_sequence",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "book_id",nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueStatus issueStatus;

    protected BookIssue(){
    }

    public BookIssue(
            Book book,
            Member member,
            LocalDate issueDate,
            LocalDate dueDate,
            LocalDate returnDate,
            IssueStatus issueStatus
    ){
        this.book = book;
        this.member = member;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.issueStatus = issueStatus;
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
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

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setIssueStatus(IssueStatus issueStatus) {
        this.issueStatus = issueStatus;
    }
}
