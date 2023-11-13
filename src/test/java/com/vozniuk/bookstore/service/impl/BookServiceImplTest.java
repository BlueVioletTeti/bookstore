package com.vozniuk.bookstore.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.vozniuk.bookstore.dto.book.BookDto;
import com.vozniuk.bookstore.dto.book.CreateBookRequestDto;
import com.vozniuk.bookstore.mapper.BookMapper;
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
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify save() method works")
    void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        CreateBookRequestDto requestDto = createBookRequestDto();
        Category category = createCategory();
        Book book = createBook(requestDto);
        BookDto bookDto = createBookDto(book);
        bookDto.setCategoryIds(Set.of(category.getId()));

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(categoryRepository.findAllById(any())).thenReturn(List.of(category));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto savedBookDto = bookService.save(requestDto);

        assertThat(savedBookDto).isEqualTo(bookDto);
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    @DisplayName("Verify findAll() method works correctly with non-empty repository")
    @Test
    void findAll_RepositoryNotEmpty_ReturnsListOfBookDto() {
        CreateBookRequestDto requestDto = createBookRequestDto();
        Category category = createCategory();
        Book book = createBook(requestDto);
        Set<Category> categories = Set.of(category);
        book.setCategories(categories);
        BookDto bookDto = createBookDto(book);
        bookDto.setCategoryIds(Set.of(category.getId()));

        List<Book> expectedPage = List.of(book);
        Page<Book> foundPage = new PageImpl<>(expectedPage);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(foundPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Pageable pageable = PageRequest.of(0, 20);
        List<BookDto> expected = bookService.findAll(pageable);

        assertThat(expected).isEqualTo(List.of(bookDto));
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @DisplayName("Verify findById() method works correctly with valid book id")
    @Test
    void findById_ValidId_ReturnsBookDto() {
        CreateBookRequestDto requestDto = createBookRequestDto();
        Category category = createCategory();
        Book book = createBook(requestDto);
        BookDto bookDto = createBookDto(book);
        bookDto.setCategoryIds(Set.of(category.getId()));
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expected = bookService.findById(id);

        assertThat(expected).isEqualTo(bookDto);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @DisplayName("Verify update() method works correctly with valid book id and"
            + "valid createBookRequestDto")
    @Test
    void update_ValidIdAndCreateBookRequestDto_ReturnsBookDto() {
        CreateBookRequestDto requestDto = createBookRequestDto();
        Category category = createCategory();
        Book book = createBook(requestDto);
        BookDto bookDto = createBookDto(book);
        bookDto.setCategoryIds(Set.of(category.getId()));
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expected = bookService.update(id, requestDto);

        assertThat(expected).isEqualTo(bookDto);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @DisplayName("Verify delete() method works correctly with valid book id")
    @Test
    void delete_ValidId_ReturnsBookDto() {
        Long id = 1L;
        bookService.delete(id);
        verify(bookRepository).deleteById(id);
    }

    private static BookDto createBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        return bookDto;
    }

    private static Category createCategory() {
        Category category = new Category();
        category.setId(1L);
        return category;
    }

    private static Book createBook(CreateBookRequestDto requestDto) {
        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        return book;
    }

    private static CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        String title = "Java Book";
        requestDto.setTitle(title);
        String author = "Some author";
        requestDto.setAuthor(author);
        String isbn = "9781617290459";
        requestDto.setIsbn(isbn);
        BigDecimal price = BigDecimal.valueOf(499);
        requestDto.setPrice(price);
        Category category = createCategory();
        requestDto.setCategoryIds(Set.of(category.getId()));
        return requestDto;
    }
}
