package com.vozniuk.bookstore.repository;

import com.vozniuk.bookstore.dto.book.BookDto;
import com.vozniuk.bookstore.model.Book;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify findAll() method works correctly")
    @Sql(scripts = {
            "classpath:database/books/add-book-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/remove-book-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ValidData_ReturnsBooks() {
        BookDto expected = new BookDto()
                .setId(1L)
                .setTitle("Java Book")
                .setAuthor("Some author")
                .setIsbn("9781617290459")
                .setPrice(BigDecimal.valueOf(499));

        Page<Book> actual = bookRepository.findAll(PageRequest.of(0,20));
        Assertions.assertEquals(1, actual.getTotalElements());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Sql(scripts = {
            "classpath:database/books/add-book-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/remove-book-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Verify findById() method works correctly with valid book id")
    void findById_ValidId_ReturnsBook() {
        Long id = 1L;
        Optional<Book> actual = bookRepository.findById(id);
        Assertions.assertEquals(id, actual.get().getId());
    }

}
