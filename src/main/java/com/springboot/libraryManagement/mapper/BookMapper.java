package com.springboot.libraryManagement.mapper;

import com.springboot.libraryManagement.dto.request.BookDto;
import com.springboot.libraryManagement.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public static Book convertDtoToBook(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.title());
        book.setAuthor(bookDto.author());
        book.setIsbn(bookDto.isbn());
        book.setPublicationYear(bookDto.publicationYear());
        book.setAvailableCopies(bookDto.availableCopies());

        return book;
    }

}
