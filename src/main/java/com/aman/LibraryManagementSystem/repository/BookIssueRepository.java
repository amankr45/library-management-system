package com.aman.LibraryManagementSystem.repository;

import com.aman.LibraryManagementSystem.entity.BookIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookIssueRepository extends JpaRepository<BookIssue,Long> {

    List<BookIssue> findByBookIdOrderByIssueDateDesc(Long bookId);

    List<BookIssue> findByMemberIdOrderByIssueDateDesc(Long memberId);
}
