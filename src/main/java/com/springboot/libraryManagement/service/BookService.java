package com.springboot.libraryManagement.service;

import com.springboot.libraryManagement.dto.request.BookDto;
import com.springboot.libraryManagement.dto.response.BookRespDto;
import com.springboot.libraryManagement.enums.Role;
import com.springboot.libraryManagement.exception.OwnershipException;
import com.springboot.libraryManagement.exception.ResourceNotFoundException;
import com.springboot.libraryManagement.mapper.BookMapper;
import com.springboot.libraryManagement.model.Book;
import com.springboot.libraryManagement.model.User;
import com.springboot.libraryManagement.repository.BookRepository;
import com.springboot.libraryManagement.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public void add(@Valid BookDto bookDto, String loggedUsername) {
        // 1. check the role
        User user = userRepository.loadUserByUsername(loggedUsername, true);
        if(!user.getRole().equals(Role.ADMIN))
            throw new OwnershipException("Operation not allowed");

        // 2. convert dto into book
        Book book = BookMapper.convertDtoToBook(bookDto);

        // 3. save in db
        bookRepository.save(book);
    }

    public List<BookRespDto> getAll(int page, int size) {
        // 1. set up the page
        Pageable pageable = PageRequest.of(page, size);

        // 2. return the books
        return bookRepository.getAll(pageable);
    }

    public BookRespDto getByIsbn(String isbn) {
        return bookRepository.getByIsbn(isbn);
    }

    public void delete(String isbn, String loggedUsername) {
        // 1. check the role
        User user = userRepository.loadUserByUsername(loggedUsername, true);
        if(!user.getRole().equals(Role.ADMIN))
            throw new OwnershipException("Operation not allowed");

        // 2. check the book exists
        Book book = bookRepository.getBook(isbn);
        if(book == null)
            throw new ResourceNotFoundException("Book not found");

        // 3. delete the book
        bookRepository.delete(book);
    }

    public void update(String isbn, Integer newAvailable, String loggedUsername) {
        // 1. check the role
        User user = userRepository.loadUserByUsername(loggedUsername, true);
        if(!user.getRole().equals(Role.ADMIN))
            throw new OwnershipException("Operation not allowed");

        // 2. check the book exists
        Book book = bookRepository.getBook(isbn);
        if(book == null)
            throw new ResourceNotFoundException("Book not found");

        // 3. update and save
        book.setAvailableCopies(newAvailable);
        bookRepository.save(book);
    }
}
