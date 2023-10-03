package com.vozniuk.bookstore.service;

import com.vozniuk.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.vozniuk.bookstore.dto.category.CategoryRequestDto;
import com.vozniuk.bookstore.dto.category.CategoryResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryDto);

    CategoryResponseDto update(Long id, CategoryRequestDto categoryDto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id);
}
