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
import com.vozniuk.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.vozniuk.bookstore.dto.category.CategoryRequestDto;
import com.vozniuk.bookstore.dto.category.CategoryResponseDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
class CategoryControllerTest {
    protected static MockMvc mockMvc;

    private static final String DEFAULT_NAME = "Category name";

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
    @Sql(scripts = {
            "classpath:database/categories/add-categories-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/categories/remove-all-from-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_NonEmptyRepository_ReturnsAllCategories() throws Exception {
        List<CategoryResponseDto> expected = new ArrayList<>();
        expected.add(createCategoryResponseDto(1L));
        expected.add(createCategoryResponseDto(2L));
        expected.add(createCategoryResponseDto(3L));

        MvcResult result = mockMvc.perform(
                        get("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto[] actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(),
                        CategoryResponseDto[].class);
        assertEquals(3, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Verify getCategoryById() method works correctly")
    @Sql(scripts = {
            "classpath:database/categories/add-categories-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/categories/remove-all-from-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_ValidId_ReturnsCategory() throws Exception {
        CategoryResponseDto expected = createCategoryResponseDto(2L);

        MvcResult result = mockMvc.perform(
                        get("/api/categories/2")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), CategoryResponseDto.class);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify createCategory() method works correctly")
    @Sql(scripts = {
            "classpath:database/categories/remove-all-from-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequestDto_Success() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto()
                .setName("Category name 1");
        CategoryResponseDto expected = createCategoryResponseDto(1L);

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/api/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), CategoryResponseDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected.getName(), actual.getName());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify updateCategory() method works correctly")
    @Sql(scripts = {
            "classpath:database/categories/add-categories-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/categories/remove-all-from-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ValidRequestDto_Success() throws Exception {
        CategoryResponseDto expected = new CategoryResponseDto()
                .setId(1L)
                .setName("Category name One");
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto()
                .setName("Category name One");

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/api/categories/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), CategoryResponseDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected.getName(), actual.getName());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify deleteCategory() method works correctly")
    @Sql(scripts = {
            "classpath:database/categories/add-categories-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/categories/remove-all-from-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_ValidId_Success() throws Exception {
        MvcResult result = mockMvc.perform(
                        delete("/api/categories/2")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Verify getBooksByCategoryId() method works correctly")
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
    void getBooksByCategoryId_ValidId_ReturnsBooks() throws Exception {
        BookDtoWithoutCategoryIds expected = new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setTitle("Another Fine Myth")
                .setAuthor("Robert Asprin")
                .setIsbn("9781617290459")
                .setPrice(BigDecimal.valueOf(499));

        MvcResult result = mockMvc.perform(
                        get("/api/categories/1/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDtoWithoutCategoryIds[] actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(),
                        BookDtoWithoutCategoryIds[].class);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList());
    }

    private CategoryResponseDto createCategoryResponseDto(long id) {
        return new CategoryResponseDto()
                .setId(id)
                .setName(DEFAULT_NAME + " " + id);
    }
}
