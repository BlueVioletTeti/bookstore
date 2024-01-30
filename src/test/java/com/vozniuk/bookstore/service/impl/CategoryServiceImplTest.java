package com.vozniuk.bookstore.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.vozniuk.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.vozniuk.bookstore.dto.book.CreateBookRequestDto;
import com.vozniuk.bookstore.dto.category.CategoryRequestDto;
import com.vozniuk.bookstore.dto.category.CategoryResponseDto;
import com.vozniuk.bookstore.mapper.BookMapper;
import com.vozniuk.bookstore.mapper.CategoryMapper;
import com.vozniuk.bookstore.model.Book;
import com.vozniuk.bookstore.model.Category;
import com.vozniuk.bookstore.repository.BookRepository;
import com.vozniuk.bookstore.repository.CategoryRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private static final String DEFAULT_NAME = "Category name";

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify save() method works correctly")
    void save_ValidCategoryRequestDto_ReturnsCategoryResponseDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName(DEFAULT_NAME);
        Category category = new Category()
                .setName(requestDto.getName());
        CategoryResponseDto categoryResponseDto = createCategoryResponseDto(1L);

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryResponseDto savedCategory = categoryService.save(requestDto);

        assertThat(savedCategory).isEqualTo(categoryResponseDto);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify findAll() method works correctly")
    void findAll_RepositoryNotEmpty_ReturnsListOfCategoryResponseDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName(DEFAULT_NAME);
        Category category = new Category()
                .setName(requestDto.getName());
        CategoryResponseDto categoryResponseDto = createCategoryResponseDto(1L);

        List<Category> expectedPage = List.of(category);
        Page<Category> foundPage = new PageImpl<>(expectedPage);

        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(foundPage);

        Pageable pageable = PageRequest.of(0, 20);
        List<CategoryResponseDto> expected = categoryService.findAll(pageable);

        assertThat(expected).isEqualTo(List.of(categoryResponseDto));
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify getById() method works correctly")
    void getById_ValidId_ReturnsCategoryResponseDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName(DEFAULT_NAME);
        Category category = new Category()
                .setName(requestDto.getName());
        Long id = 1L;
        CategoryResponseDto categoryResponseDto = createCategoryResponseDto(id);

        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        CategoryResponseDto savedCategory = categoryService.getById(id);

        assertThat(savedCategory).isEqualTo(categoryResponseDto);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify update() method works correctly")
    void update_ValidIdAndCategoryRequestDto_ReturnsCategoryResponseDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName(DEFAULT_NAME);
        Category category = new Category()
                .setName(requestDto.getName());
        Long id = 1L;
        CategoryResponseDto categoryResponseDto = createCategoryResponseDto(id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto savedCategory = categoryService.update(id, requestDto);

        assertThat(savedCategory).isEqualTo(categoryResponseDto);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify deleteById() method works correctly")
    void deleteById_ValidId_ReturnsCategoryResponseDto() {
        Long id = 1L;
        categoryService.deleteById(id);
        verify(categoryRepository).deleteById(id);
    }

    @Test
    @DisplayName("Verify getBooksByCategoryId() method works correctly")
    void getBooksByCategoryId_ValidId_ReturnsBookDtoWithoutCategoryIds() {
        Long id = 1L;
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto()
                .setTitle("Java Book")
                .setAuthor("Some author")
                .setIsbn("9781617290459")
                .setPrice(BigDecimal.valueOf(499))
                .setCategoryIds(Set.of(id));
        Book book = new Book()
                .setTitle(createBookRequestDto.getTitle())
                .setAuthor(createBookRequestDto.getAuthor())
                .setIsbn(createBookRequestDto.getIsbn())
                .setPrice(createBookRequestDto.getPrice());

        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds()
                .setTitle("Java Book")
                .setAuthor("Some author")
                .setIsbn("9781617290459")
                .setPrice(BigDecimal.valueOf(499));

        when(categoryRepository.findAllByCategoryId(id)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> actual = categoryService.getBooksByCategoryId(id);

        assertThat(actual).isEqualTo(List.of(bookDtoWithoutCategoryIds));
        verifyNoMoreInteractions(categoryRepository, bookMapper);
    }

    private CategoryResponseDto createCategoryResponseDto(long id) {
        return new CategoryResponseDto()
                .setId(id)
                .setName(DEFAULT_NAME + " " + id);
    }
}
