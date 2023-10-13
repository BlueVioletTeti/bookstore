package com.vozniuk.bookstore.mapper;

import com.vozniuk.bookstore.config.MapperConfig;
import com.vozniuk.bookstore.dto.order.OrderItemDto;
import com.vozniuk.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    OrderItem toEntity(OrderItemDto orderItemDto);
}
