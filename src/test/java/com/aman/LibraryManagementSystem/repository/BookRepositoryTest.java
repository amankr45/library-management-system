package com.aman.LibraryManagementSystem.repository;

import com.aman.LibraryManagementSystem.entity.Book;
import com.aman.LibraryManagementSystem.enums.BookCategory;
import com.aman.LibraryManagementSystem.enums.BookStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    // ============================================================
    // SAVE
    // ============================================================

    @Test
    void shouldSaveBookSuccessfully() {

        Book book = new Book(
                "Clean Code",
                "Robert C. Martin",
                "TEST-BOOK-001",
                BookCategory.PROGRAMMING
        );

        Book savedBook = bookRepository.save(book);

        assertThat(savedBook.getId())
                .isNotNull();

        assertThat(savedBook.getTitle())
                .isEqualTo("Clean Code");

        assertThat(savedBook.getAuthor())
                .isEqualTo("Robert C. Martin");

        assertThat(savedBook.getIsbn())
                .isEqualTo("TEST-BOOK-001");

        assertThat(savedBook.getCategory())
                .isEqualTo(BookCategory.PROGRAMMING);

        assertThat(savedBook.getStatus())
                .isEqualTo(BookStatus.AVAILABLE);
    }

    // ============================================================
    // FIND BY ID
    // ============================================================

    @Test
    void shouldFindBookByIdSuccessfully() {

        Book book = bookRepository.save(
                new Book(
                        "Effective Java",
                        "Joshua Bloch",
                        "TEST-BOOK-002",
                        BookCategory.PROGRAMMING
                )
        );

        Optional<Book> result =
                bookRepository.findById(book.getId());

        assertThat(result)
                .isPresent();

        assertThat(result.get().getTitle())
                .isEqualTo("Effective Java");
    }

    // ============================================================
    // FIND ALL
    // ============================================================

    @Test
    void shouldFindAllBooksSuccessfully() {

        bookRepository.save(
                new Book(
                        "Clean Architecture",
                        "Robert C. Martin",
                        "TEST-BOOK-003",
                        BookCategory.PROGRAMMING
                )
        );

        bookRepository.save(
                new Book(
                        "The Hobbit",
                        "J.R.R. Tolkien",
                        "TEST-BOOK-004",
                        BookCategory.FICTION
                )
        );

        List<Book> books =
                bookRepository.findAll();

        assertThat(books)
                .hasSize(2);
    }

    // ============================================================
    // FIND BY TITLE
    // ============================================================

    @Test
    void shouldFindBooksByTitle() {

        Book book = bookRepository.save(
                new Book(
                        "Clean Code",
                        "Robert C. Martin",
                        "TEST-BOOK-005",
                        BookCategory.PROGRAMMING
                )
        );

        List<Book> books =
                bookRepository.findByTitle("Clean Code");

        assertThat(books)
                .hasSize(1);

        assertThat(books.get(0).getId())
                .isEqualTo(book.getId());
    }

    // ============================================================
    // FIND BY AUTHOR
    // ============================================================

    @Test
    void shouldFindBooksByAuthor() {

        bookRepository.save(
                new Book(
                        "Clean Code",
                        "Robert C. Martin",
                        "TEST-BOOK-006",
                        BookCategory.PROGRAMMING
                )
        );

        bookRepository.save(
                new Book(
                        "Clean Architecture",
                        "Robert C. Martin",
                        "TEST-BOOK-007",
                        BookCategory.PROGRAMMING
                )
        );

        List<Book> books =
                bookRepository.findByAuthor("Robert C. Martin");

        assertThat(books)
                .hasSize(2);

        assertThat(books)
                .extracting(Book::getAuthor)
                .containsOnly("Robert C. Martin");
    }

    // ============================================================
    // FIND BY CATEGORY
    // ============================================================

    @Test
    void shouldFindBooksByCategory() {

        bookRepository.save(
                new Book(
                        "Clean Code",
                        "Robert C. Martin",
                        "TEST-BOOK-008",
                        BookCategory.PROGRAMMING
                )
        );

        bookRepository.save(
                new Book(
                        "The Hobbit",
                        "J.R.R. Tolkien",
                        "TEST-BOOK-009",
                        BookCategory.FICTION
                )
        );

        List<Book> books =
                bookRepository.findByCategory(
                        BookCategory.PROGRAMMING
                );

        assertThat(books)
                .hasSize(1);

        assertThat(books.get(0).getCategory())
                .isEqualTo(BookCategory.PROGRAMMING);
    }

    // ============================================================
    // FIND BY STATUS
    // ============================================================

    @Test
    void shouldFindBooksByStatus() {

        Book availableBook = bookRepository.save(
                new Book(
                        "Available Book",
                        "Test Author",
                        "TEST-BOOK-010",
                        BookCategory.FICTION
                )
        );

        Book issuedBook = bookRepository.save(
                new Book(
                        "Issued Book",
                        "Test Author",
                        "TEST-BOOK-011",
                        BookCategory.FICTION
                )
        );

        issuedBook.setStatus(BookStatus.ISSUED);

        bookRepository.save(issuedBook);

        List<Book> availableBooks =
                bookRepository.findByStatus(
                        BookStatus.AVAILABLE
                );

        List<Book> issuedBooks =
                bookRepository.findByStatus(
                        BookStatus.ISSUED
                );

        assertThat(availableBooks)
                .hasSize(1);

        assertThat(availableBooks.getFirst().getId())
                .isEqualTo(availableBook.getId());

        assertThat(issuedBooks)
                .hasSize(1);

        assertThat(issuedBooks.getFirst().getId())
                .isEqualTo(issuedBook.getId());
    }

    // ============================================================
    // EXISTS BY ISBN
    // ============================================================

    @Test
    void shouldReturnTrueWhenIsbnExists() {

        bookRepository.save(
                new Book(
                        "Domain Driven Design",
                        "Eric Evans",
                        "TEST-BOOK-012",
                        BookCategory.PROGRAMMING
                )
        );

        boolean exists =
                bookRepository.existsByIsbn(
                        "TEST-BOOK-012"
                );

        assertThat(exists)
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenIsbnDoesNotExist() {

        boolean exists =
                bookRepository.existsByIsbn(
                        "NON-EXISTING-ISBN"
                );

        assertThat(exists)
                .isFalse();
    }

    // ============================================================
    // EMPTY RESULT TESTS
    // ============================================================

    @Test
    void shouldReturnEmptyListWhenTitleDoesNotExist() {

        List<Book> books =
                bookRepository.findByTitle(
                        "Non Existing Book"
                );

        assertThat(books)
                .isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenAuthorDoesNotExist() {

        List<Book> books =
                bookRepository.findByAuthor(
                        "Non Existing Author"
                );

        assertThat(books)
                .isEmpty();
    }
}