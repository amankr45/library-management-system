package com.aman.LibraryManagementSystem.exception.handler;

import com.aman.LibraryManagementSystem.dto.error.ErrorResponse;
import com.aman.LibraryManagementSystem.exception.book.BookNotFoundException;
import com.aman.LibraryManagementSystem.exception.book.DuplicateBookException;
import com.aman.LibraryManagementSystem.exception.issue.BookAlreadyIssuedException;
import com.aman.LibraryManagementSystem.exception.issue.BookAlreadyReturnedException;
import com.aman.LibraryManagementSystem.exception.issue.BookIssueNotFoundException;
import com.aman.LibraryManagementSystem.exception.member.DuplicateMemberException;
import com.aman.LibraryManagementSystem.exception.member.MemberNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateMemberException(
            DuplicateMemberException exception,
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

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(
            MemberNotFoundException exception,
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

    @ExceptionHandler(BookIssueNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookIssueNotFound(
            BookIssueNotFoundException exception,
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

    @ExceptionHandler(BookAlreadyIssuedException.class)
    public ResponseEntity<ErrorResponse> handleBookAlreadyIssued(
            BookAlreadyIssuedException exception,
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

    @ExceptionHandler(BookAlreadyReturnedException.class)
    public ResponseEntity<ErrorResponse> handleBookAlreadyReturned(
            BookAlreadyReturnedException exception,
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


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> fieldErrors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {

                    String field = error.getField();
                    String errorCode = error.getCode();

                    String existingError =
                            fieldErrors.get(field);

                    if (existingError == null
                            || "NotBlank".equals(errorCode)) {

                        fieldErrors.put(
                                field,
                                error.getDefaultMessage()
                        );
                    }
                });

        ErrorResponse response = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request
        );

        response.setFieldErrors(fieldErrors);

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    private ErrorResponse buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }
}