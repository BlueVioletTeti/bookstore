package com.vozniuk.bookstore.repository;

import com.vozniuk.bookstore.dto.book.BookDto;
import com.vozniuk.bookstore.dto.category.CategoryResponseDto;
import com.vozniuk.bookstore.model.Book;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Verify findAllByCategoryId() method works correctly")
    @Sql(scripts = {
            "classpath:database/categories/add-book-to-books-table.sql",
            "classpath:database/categories/add-category-to-categories-table.sql",
            "classpath:database/categories/add-ids-to-book_category-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/categories/remove-all-from-book_category.sql",
            "classpath:database/categories/remove-all-from-categories.sql",
            "classpath:database/categories/remove-all-from-books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_ValidData_ReturnsCategories() {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto()
                .setId(1L)
                .setName("Sci-Fi")
                .setDescription("Science Fiction");
        BookDto expected = new BookDto()
                .setId(1L)
                .setTitle("Another Fine Myth")
                .setAuthor("Robert Asprin")
                .setIsbn("9781617290459")
                .setPrice(BigDecimal.valueOf(499))
                .setCategoryIds(Set.of(categoryResponseDto.getId()));
        List<Book> actual = categoryRepository.findAllByCategoryId(1L);
        Assertions.assertEquals(1, actual.size());
        EqualsBuilder.reflectionEquals(expected, actual);
    }
}
