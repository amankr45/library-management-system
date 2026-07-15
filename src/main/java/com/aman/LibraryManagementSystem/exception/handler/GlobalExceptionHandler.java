package com.aman.LibraryManagementSystem.exception.handler;

import com.aman.LibraryManagementSystem.dto.error.ErrorResponse;
import com.aman.LibraryManagementSystem.exception.book.BookNotFoundException;
import com.aman.LibraryManagementSystem.exception.book.DuplicateBookException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFoundException(
            BookNotFoundException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                exception.getMessage(),
                                request
                        )
                );
    }

    @ExceptionHandler(DuplicateBookException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateBookException(
            DuplicateBookException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        buildErrorResponse(
                                HttpStatus.CONFLICT,
                                exception.getMessage(),
                                request
                        )
                );
    }

    private ErrorResponse buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setTimestamp(
                LocalDateTime.now()
        );

        errorResponse.setStatus(
                status.value()
        );

        errorResponse.setError(
                status.getReasonPhrase()
        );

        errorResponse.setMessage(
                message
        );

        errorResponse.setPath(
                request.getRequestURI()
        );

        return errorResponse;
    }

}