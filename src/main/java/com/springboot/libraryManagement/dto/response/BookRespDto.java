package com.springboot.libraryManagement.dto.response;

public record BookRespDto(
        String title,
        String author,
        String isbn,
        Integer publicationYear,
        Integer availableCopies
) {
}
