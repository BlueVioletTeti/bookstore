package com.vozniuk.bookstore.mapper;

import com.vozniuk.bookstore.config.MapperConfig;
import com.vozniuk.bookstore.dto.order.OrderResponseDto;
import com.vozniuk.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toDto(Order order);

    @Mapping(source = "userId", target = "user.id")
    Order toEntity(OrderResponseDto orderResponseDto);
}
