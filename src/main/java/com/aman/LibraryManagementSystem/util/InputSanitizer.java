package com.aman.LibraryManagementSystem.util;

public final class InputSanitizer {

    private InputSanitizer() {
    }

    public static String sanitize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim()
                .replaceAll("\\s+", " ");
    }

    public static String normalizeIsbn(String isbn) {
        if (isbn == null) {
            return null;
        }
        return sanitize(isbn)
                .replace("-", "");
    }
}