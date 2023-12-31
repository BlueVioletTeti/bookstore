package com.vozniuk.bookstore.service.impl;

import com.vozniuk.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.vozniuk.bookstore.dto.category.CategoryRequestDto;
import com.vozniuk.bookstore.dto.category.CategoryResponseDto;
import com.vozniuk.bookstore.mapper.BookMapper;
import com.vozniuk.bookstore.mapper.CategoryMapper;
import com.vozniuk.bookstore.model.Category;
import com.vozniuk.bookstore.repository.BookRepository;
import com.vozniuk.bookstore.repository.CategoryRepository;
import com.vozniuk.bookstore.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @Override
    public CategoryResponseDto save(CategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponseDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find a category with id: " + id)));
    }

    @Transactional
    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't update a category with id: " + id));
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        return categoryRepository.findAllByCategoryId(id).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
