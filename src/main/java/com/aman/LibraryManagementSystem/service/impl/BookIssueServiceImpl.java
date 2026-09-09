package com.aman.LibraryManagementSystem.service.impl;

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
import com.aman.LibraryManagementSystem.service.BookIssueService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookIssueServiceImpl implements BookIssueService {

    private static final long ISSUE_DURATION_DAYS = 14;

    private final BookIssueRepository bookIssueRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BookIssueMapper bookIssueMapper;

    public BookIssueServiceImpl(
            BookIssueRepository bookIssueRepository,
            BookRepository bookRepository,
            MemberRepository memberRepository,
            BookIssueMapper bookIssueMapper
    ){
        this.bookIssueRepository = bookIssueRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.bookIssueMapper = bookIssueMapper;
    }

    @Override
    @Transactional
    public BookIssueResponse issueBook(BookIssueRequest request){
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() ->
                        new BookNotFoundException(
                                request.getBookId()
                        )
                );

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() ->
                        new MemberNotFoundException(
                                request.getMemberId()
                        )
                );

        if(book.getStatus() != BookStatus.AVAILABLE){
            throw new BookAlreadyIssuedException(
                    "Book cannot be issued because its current status is : "
                    + book.getStatus()
            );
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(ISSUE_DURATION_DAYS);

        BookIssue bookIssue = bookIssueMapper.toEntity(
                request,
                book,
                member,
                issueDate,
                dueDate,
                IssueStatus.ISSUED
        );

        book.setStatus(BookStatus.ISSUED);

        BookIssue savedBookIssue = bookIssueRepository.save(bookIssue);

        return bookIssueMapper.toResponse(savedBookIssue);
    }

    @Override
    @Transactional
    public BookIssueResponse returnBook(Long issueId){
        BookIssue bookIssue = bookIssueRepository.findById(issueId)
                .orElseThrow(() ->
                        new BookIssueNotFoundException(issueId)
                );

        if(bookIssue.getIssueStatus() == IssueStatus.RETURNED){
            throw new BookAlreadyReturnedException(
                    "Book issue has already been returned with id : " + issueId
            );
        }

        LocalDate returnDate = LocalDate.now();

        bookIssue.setReturnDate(returnDate);
        bookIssue.setIssueStatus(IssueStatus.RETURNED);

        Book book = bookIssue.getBook();
        book.setStatus(BookStatus.AVAILABLE);

        BookIssue savedBookIssue = bookIssueRepository.save(bookIssue);

        return bookIssueMapper.toResponse(savedBookIssue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookIssueResponse> getAllBookIssues(){
        return bookIssueRepository.findAll()
                .stream()
                .map(bookIssueMapper :: toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookIssueResponse getBookIssueById(Long issueId){

        BookIssue bookIssue = bookIssueRepository.findById(issueId)
                .orElseThrow(() ->
                    new BookIssueNotFoundException(issueId)
                );

        return bookIssueMapper.toResponse(bookIssue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookIssueResponse> getBookIssueHistoryByMember(Long memberId){
        memberRepository.findById(memberId)
                .orElseThrow(() ->
                    new MemberNotFoundException(memberId)
                );

        return bookIssueRepository
                .findByMemberIdOrderByIssueDateDesc(memberId)
                .stream()
                .map(bookIssueMapper :: toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookIssueResponse> getBookIssueHistoryByBook(Long bookId){
        bookRepository.findById(bookId)
                .orElseThrow(() ->
                    new BookNotFoundException(bookId)
                );

        return bookIssueRepository
                .findByBookIdOrderByIssueDateDesc(bookId)
                .stream()
                .map(bookIssueMapper :: toResponse)
                .toList();
    }
}
