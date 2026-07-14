package com.aman.LibraryManagementSystem.entity;

import com.aman.LibraryManagementSystem.enums.BookCategory;
import com.aman.LibraryManagementSystem.enums.BookStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "bookSeqGen"
    )
    @SequenceGenerator(
            name = "bookSeqGen",
            sequenceName = "book_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(
            name = "title",
            nullable = false,
            length = 200
    )
    private String title;

    @Column(
            name = "author",
            nullable = false,
            length = 100
    )
    private String author;

    @Column(
            name = "isbn",
            nullable = false,
            unique = true,
            length = 20
    )
    private String isbn;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "category",
            nullable = false
    )
    private BookCategory category;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false
    )
    private BookStatus status;

    protected Book() {
    }

    public Book(
            String title,
            String author,
            String isbn,
            BookCategory category
    ) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.status = BookStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", category=" + category +
                ", status=" + status +
                '}';
    }
}
