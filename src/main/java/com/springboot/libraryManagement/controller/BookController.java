package com.springboot.libraryManagement.controller;

import com.springboot.libraryManagement.dto.request.BookDto;
import com.springboot.libraryManagement.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/api/book/add")
    public void add(@Valid@RequestBody BookDto bookDto,
                    Principal principal) {
        bookService.add(bookDto, principal.getName());
    }

}
