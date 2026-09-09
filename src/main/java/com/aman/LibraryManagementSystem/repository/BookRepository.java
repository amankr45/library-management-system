package com.aman.LibraryManagementSystem.repository;

import com.aman.LibraryManagementSystem.entity.Book;
import com.aman.LibraryManagementSystem.enums.BookCategory;
import com.aman.LibraryManagementSystem.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByCategory(BookCategory category);
    List<Book> findByStatus(BookStatus status);
    boolean existsByIsbn(String isbn);
}