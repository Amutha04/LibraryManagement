package com.springboot.libraryManagement.service;

import com.springboot.libraryManagement.dto.request.BookDto;
import com.springboot.libraryManagement.enums.Role;
import com.springboot.libraryManagement.exception.OwnershipException;
import com.springboot.libraryManagement.mapper.BookMapper;
import com.springboot.libraryManagement.model.Book;
import com.springboot.libraryManagement.model.User;
import com.springboot.libraryManagement.repository.BookRepository;
import com.springboot.libraryManagement.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
