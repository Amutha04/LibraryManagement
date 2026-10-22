package com.springboot.libraryManagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookDto(
        @NotNull(message = "Title is required")
        @NotBlank(message = "Title is required")
        String title,
        @NotNull(message = "Author is required")
        @NotBlank(message = "Author is required")
        String author,
        @NotNull(message = "ISBN is required")
        @NotBlank(message = "ISBN is required")
        String isbn,
        @NotNull(message = "Publication Year is required")
        Integer publicationYear,
        @NotNull(message = "Available copies is required")
        Integer availableCopies
) {
}
