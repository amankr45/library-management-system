package com.aman.LibraryManagementSystem.repository.specification;

import com.aman.LibraryManagementSystem.entity.Book;
import com.aman.LibraryManagementSystem.enums.BookCategory;
import com.aman.LibraryManagementSystem.enums.BookStatus;
import org.springframework.data.jpa.domain.Specification;

public final class BookSpecifications {

    private BookSpecifications() {
    }

    public static Specification<Book> search(String search) {

        if (search == null || search.isBlank()) {
            return null;
        }

        String value = "%" + search.trim().toLowerCase() + "%";

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                value
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("author")),
                                value
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("isbn")),
                                value
                        )
                );
    }

    public static Specification<Book> hasCategory(
            BookCategory category
    ) {

        if (category == null) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("category"),
                        category
                );
    }

    public static Specification<Book> hasStatus(
            BookStatus status
    ) {

        if (status == null) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }
}