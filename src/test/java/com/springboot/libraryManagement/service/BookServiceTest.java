package com.springboot.libraryManagement.service;

import com.springboot.libraryManagement.dto.request.BookDto;
import com.springboot.libraryManagement.dto.response.BookRespDto;
import com.springboot.libraryManagement.enums.Role;
import com.springboot.libraryManagement.exception.OwnershipException;
import com.springboot.libraryManagement.exception.ResourceNotFoundException;
import com.springboot.libraryManagement.model.Book;
import com.springboot.libraryManagement.model.User;
import com.springboot.libraryManagement.repository.BookRepository;
import com.springboot.libraryManagement.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;

    @Test
    public void addTest() {
        BookDto bookDto1 = new BookDto("title", "author", "1234567890123", 2004, 10);
        String loggedUsername = "username";
        User user = new User();
        user.setRole(Role.ADMIN);
        Book book = new Book();

        when(userRepository.loadUserByUsername(loggedUsername, true)).thenReturn(user);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.add(bookDto1, loggedUsername);

        verify(userRepository, times(1)).loadUserByUsername(loggedUsername, true);
        verify(bookRepository, times(1)).save(any(Book.class));

        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(bookRepository);

    }

    @Test
    public void addExceptionTest() {
        BookDto bookDto1 = new BookDto("title", "author", "1234567890123", 2004, 10);
        String loggedUsername = "username";
        User user = new User();
        user.setRole(Role.STUDENT);
        Book book = new Book();

        when(userRepository.loadUserByUsername(loggedUsername, true)).thenThrow(OwnershipException.class);

        assertThrows(OwnershipException.class, () -> bookService.add(bookDto1, loggedUsername));

        verify(userRepository, times(1)).loadUserByUsername(loggedUsername, true);
        verify(bookRepository, times(0)).save(book);

        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void getAllTest() {
        BookRespDto dto1 = new BookRespDto("title", "author", "1234567890123", 2004, 10);
        BookRespDto dto2 = new BookRespDto("title", "author", "1034567890123", 2014, 5);

        when(bookRepository.getAll(PageRequest.of(0,20)))
                .thenReturn(List.of(dto1, dto2));

        assertEquals(2, bookRepository.getAll(PageRequest.of(0,20)).size());

        verify(bookRepository, times(1)).getAll(PageRequest.of(0,20));

        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void getByIsbnTest() {
        BookRespDto dto1 = new BookRespDto("title", "author", "1234567890123", 2004, 10);
        String isbn = "1234567890123";
        Book book = new Book();

        when(bookRepository.getByIsbn(isbn)).thenReturn(dto1);
        when(bookRepository.getBook(isbn)).thenReturn(book);

        assertEquals(dto1, bookRepository.getByIsbn(isbn));
        assertEquals(book, bookRepository.getBook(isbn));

        verify(bookRepository, times(1)).getByIsbn(isbn);
        verify(bookRepository, times(1)).getBook(isbn);

        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void getByIsbnExceptionTest() {
        String isbn = "1234567890123";

        when(bookRepository.getBook(isbn)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> bookService.getByIsbn(isbn));

        verify(bookRepository, times(1)).getBook(isbn);
        verify(bookRepository, times(0)).getByIsbn(isbn);

        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void deleteTest() {
        String loggedUsername = "username";
        String isbn = "1234567890123";
        User user = new User();
        user.setRole(Role.ADMIN);
        Book book = new Book();
        book.setId(1L);

        when(userRepository.loadUserByUsername(loggedUsername, true)).thenReturn(user);
        when(bookRepository.getBook(isbn)).thenReturn(book);

        bookService.delete(isbn, loggedUsername);

        verify(userRepository, times(1)).loadUserByUsername(loggedUsername, true);
        verify(bookRepository, times(1)).getBook(isbn);
        verify(bookRepository, times(1)).delete(book);

        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void deleteRoleExceptionTest() {
        String loggedUsername = "username";
        String isbn = "1234567890123";
        User user = new User();
        user.setRole(Role.STUDENT);

        when(userRepository.loadUserByUsername(loggedUsername, true)).thenThrow(OwnershipException.class);

        assertThrows(OwnershipException.class, () -> bookService.delete(isbn, loggedUsername));

        verify(userRepository, times(1)).loadUserByUsername(loggedUsername, true);

        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    public void deleteResourceExceptionTest() {
        String loggedUsername = "username";
        String isbn = "1234567890123";
        User user = new User();
        user.setRole(Role.ADMIN);

        when(userRepository.loadUserByUsername(loggedUsername, true)).thenReturn(user);
        when(bookRepository.getBook(isbn)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> bookService.delete(isbn, loggedUsername));

        verify(userRepository, times(1)).loadUserByUsername(loggedUsername, true);
        verify(bookRepository, times(1)).getBook(isbn);
        verify(bookRepository, times(0)).delete(any(Book.class));

        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void updateTest() {
        String loggedUsername = "username";
        Integer newAvailable = 10;
        String isbn = "1234567890123";
        User user = new User();
        user.setRole(Role.ADMIN);
        Book book = new Book();
        book.setId(1L);

        when(userRepository.loadUserByUsername(loggedUsername, true)).thenReturn(user);
        when(bookRepository.getBook(isbn)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.update(isbn,newAvailable,loggedUsername);

        verify(userRepository, times(1)).loadUserByUsername(loggedUsername, true);
        verify(bookRepository, times(1)).getBook(isbn);
        verify(bookRepository, times(1)).save(book);

        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void updateTestRoleException() {
        String loggedUsername = "username";
        String isbn = "1234567890123";
        User user = new User();
        user.setRole(Role.STUDENT);

        when(userRepository.loadUserByUsername(loggedUsername, true)).thenThrow(OwnershipException.class);

        assertThrows(OwnershipException.class, () -> bookService.update(isbn, 10, loggedUsername));

        verify(userRepository, times(1)).loadUserByUsername(loggedUsername, true);

        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    public void updateTestResourceException() {
        String loggedUsername = "username";
        String isbn = "1234567890123";
        User user = new User();
        user.setRole(Role.ADMIN);

        when(userRepository.loadUserByUsername(loggedUsername, true)).thenReturn(user);
        when(bookRepository.getBook(isbn)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> bookService.update(isbn, 10, loggedUsername));

        verify(userRepository, times(1)).loadUserByUsername(loggedUsername, true);
        verify(bookRepository, times(1)).getBook(isbn);
        verify(bookRepository, times(0)).save(any(Book.class));

        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(bookRepository);
    }

}
