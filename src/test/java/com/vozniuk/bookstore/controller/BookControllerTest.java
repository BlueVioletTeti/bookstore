package com.vozniuk.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vozniuk.bookstore.dto.book.BookDto;
import com.vozniuk.bookstore.dto.book.CreateBookRequestDto;
import com.vozniuk.bookstore.model.Book;
import com.vozniuk.bookstore.model.Category;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Verify getAll() method works correctly")
    @Sql(scripts = "classpath:database/books/add-default-books-to-books-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-from-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_NonEmptyRepository_ReturnsAllBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto().setId(1L).setTitle("Java Book 1").setAuthor("Some author 1")
                .setIsbn("9781617290459").setPrice(BigDecimal.valueOf(499))
                .setCategoryIds(new HashSet<Long>(0)));
        expected.add(new BookDto().setId(2L).setTitle("Java Book 2").setAuthor("Some author 2")
                .setIsbn("8781617290559").setPrice(BigDecimal.valueOf(599))
                .setCategoryIds(new HashSet<Long>(0)));
        expected.add(new BookDto().setId(3L).setTitle("Java Book 3").setAuthor("Some author 3")
                .setIsbn("978-3-16-148410-0").setPrice(BigDecimal.valueOf(699))
                .setCategoryIds(new HashSet<Long>(0)));

        MvcResult result = mockMvc.perform(
                get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                BookDto[].class);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Verify getBookById() method works correctly")
    @Sql(scripts = "classpath:database/books/add-default-books-to-books-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-from-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_ValidId_ReturnsBook() throws Exception {
        BookDto expected = new BookDto().setId(2L).setTitle("Java Book 2")
                .setAuthor("Some author 2").setIsbn("8781617290559")
                .setPrice(BigDecimal.valueOf(599))
                .setCategoryIds(new HashSet<Long>(0));

        MvcResult result = mockMvc.perform(
                        get("/api/books/2")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        Assertions.assertEquals(2, actual.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify createBook() method works correctly")
    @Sql(scripts = "classpath:database/books/remove-all-books-from-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = createBookRequestDto();
        Category category = createCategory();
        Book book = createBook(requestDto);
        BookDto expected = createBookDto(book);
        expected.setCategoryIds(Set.of(category.getId()));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify updateBook() method works correctly")
    @Sql(scripts = "classpath:database/books/add-default-books-to-books-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-from-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_ValidRequestDto_Success() throws Exception {
        BookDto expected = new BookDto().setId(1L).setTitle("Java Book").setAuthor("Some author")
                .setIsbn("9781617290459").setPrice(BigDecimal.valueOf(499))
                .setCategoryIds(new HashSet<Long>(0));

        CreateBookRequestDto requestDto = createBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                        put("/api/books/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify deleteBook() method works correctly")
    @Sql(scripts = "classpath:database/books/add-default-books-to-books-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-from-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBook_ValidId_Success() throws Exception {
        mockMvc.perform(delete("/api/books/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    private static Category createCategory() {
        return new Category().setId(1L);
    }

    private static CreateBookRequestDto createBookRequestDto() {
        String title = "Java Book";
        String author = "Some author";
        String isbn = "9781617290459";
        BigDecimal price = BigDecimal.valueOf(499);
        Category category = createCategory();
        return new CreateBookRequestDto()
                .setTitle(title)
                .setAuthor(author)
                .setIsbn(isbn)
                .setPrice(price)
                .setCategoryIds(Set.of(category.getId()));
    }

    private static BookDto createBookDto(Book book) {
        return new BookDto()
                .setId(1L)
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice());
    }

    private static Book createBook(CreateBookRequestDto requestDto) {
        return new Book()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice());
    }
}
