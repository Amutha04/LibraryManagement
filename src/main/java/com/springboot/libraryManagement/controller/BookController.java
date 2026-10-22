package com.springboot.libraryManagement.controller;

import com.springboot.libraryManagement.dto.request.BookDto;
import com.springboot.libraryManagement.dto.response.BookRespDto;
import com.springboot.libraryManagement.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/api/book/add")
    public void add(@Valid@RequestBody BookDto bookDto,
                    Principal principal) {
        bookService.add(bookDto, principal.getName());
    }

    @GetMapping("/api/book/all")
    public List<BookRespDto> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(name = "size", required = false, defaultValue = "20") int size
                                    ) {
        return bookService.getAll(page, size);
    }

    @GetMapping("/api/book/get")
    public BookRespDto getByIsbn(@RequestParam(name = "isbn") String isbn) {
        return bookService.getByIsbn(isbn);
    }

}
