package com.vozniuk.bookstore.mapper;

import com.vozniuk.bookstore.config.MapperConfig;
import com.vozniuk.bookstore.dto.BookDto;
import com.vozniuk.bookstore.dto.CreateBookRequestDto;
import com.vozniuk.bookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
