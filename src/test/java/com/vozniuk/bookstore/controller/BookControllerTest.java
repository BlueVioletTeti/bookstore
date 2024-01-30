package com.vozniuk.bookstore.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vozniuk.bookstore.dto.book.BookDto;
import com.vozniuk.bookstore.dto.book.CreateBookRequestDto;
import com.vozniuk.bookstore.model.Category;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    private static final String DEFAULT_TITLE = "Java Book";
    private static final String DEFAULT_AUTHOR = "Some author";
    private static final String DEFAULT_ISBN = "9781617290459";
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(499);
    private static final Category DEFAULT_CATEGORY = new Category().setId(1L);

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
    @Sql(scripts = {"classpath:database/books/add-default-books-to-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/remove-all-books-from-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_NonEmptyRepository_ReturnsAllBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(createBookDto(1L, DEFAULT_ISBN, DEFAULT_PRICE));
        expected.add(createBookDto(2L, "8781617290559", BigDecimal.valueOf(599)));
        expected.add(createBookDto(3L, "978-3-16-148410-0", BigDecimal.valueOf(699)));

        MvcResult result = mockMvc.perform(
                get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                BookDto[].class);
        assertEquals(3, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList(), "categoryIds");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Verify getBookById() method works correctly")
    @Sql(scripts = "classpath:database/books/add-default-books-to-books-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-from-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_ValidId_ReturnsBook() throws Exception {
        BookDto expected = createBookDto();

        MvcResult result = mockMvc.perform(
                        get("/api/books/2")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "categoryIds");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify createBook() method works correctly")
    @Sql(scripts = "classpath:database/books/remove-all-books-from-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = createBookRequestDto();
        BookDto expected = createBookDto();

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
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
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
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
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

    private static CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle(DEFAULT_TITLE)
                .setAuthor(DEFAULT_AUTHOR)
                .setIsbn(DEFAULT_ISBN)
                .setPrice(DEFAULT_PRICE)
                .setCategoryIds(Set.of(DEFAULT_CATEGORY.getId()));
    }

    private static BookDto createBookDto() {
        return new BookDto()
                .setId(1L)
                .setTitle(DEFAULT_TITLE)
                .setAuthor(DEFAULT_AUTHOR)
                .setIsbn(DEFAULT_ISBN)
                .setPrice(DEFAULT_PRICE)
                .setCategoryIds(Set.of(DEFAULT_CATEGORY.getId()));
    }

    private static BookDto createBookDto(Long id, String isbn, BigDecimal price) {
        return new BookDto()
                .setId(id)
                .setTitle(DEFAULT_TITLE + " " + id)
                .setAuthor(DEFAULT_AUTHOR + " " + id)
                .setIsbn(isbn)
                .setPrice(price)
                .setCategoryIds(Set.of(DEFAULT_CATEGORY.getId()));
    }
}
