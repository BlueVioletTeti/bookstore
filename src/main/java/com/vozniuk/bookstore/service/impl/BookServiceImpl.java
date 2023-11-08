package com.vozniuk.bookstore.service.impl;

import com.vozniuk.bookstore.dto.book.BookDto;
import com.vozniuk.bookstore.dto.book.CreateBookRequestDto;
import com.vozniuk.bookstore.mapper.BookMapper;
import com.vozniuk.bookstore.model.Book;
import com.vozniuk.bookstore.model.Category;
import com.vozniuk.bookstore.repository.BookRepository;
import com.vozniuk.bookstore.repository.CategoryRepository;
import com.vozniuk.bookstore.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        List<Category> categories = categoryRepository.findAllById(requestDto.getCategoryIds());
        book.setCategories(new HashSet<>(categories));
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(bookRepository.save(savedBook));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find a book with id: " + id)));
    }

    @Transactional
    @Override
    public BookDto update(Long id,CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't update a book with id: " + id));
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
