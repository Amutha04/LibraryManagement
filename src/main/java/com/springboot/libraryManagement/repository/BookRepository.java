package com.springboot.libraryManagement.repository;

import com.springboot.libraryManagement.dto.response.BookRespDto;
import com.springboot.libraryManagement.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
select b.title, b.author, b.isbn, b.publicationYear, b.availableCopies
from Book b
""")
    List<BookRespDto> getAll(Pageable pageable);

    @Query("""
select b.title, b.author, b.isbn, b.publicationYear, b.availableCopies
from Book b
where b.isbn = ?1
""")
    BookRespDto getByIsbn(String isbn);
}
