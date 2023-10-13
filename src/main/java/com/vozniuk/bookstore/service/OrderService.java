package com.vozniuk.bookstore.service;

import com.vozniuk.bookstore.dto.order.OrderItemDto;
import com.vozniuk.bookstore.dto.order.OrderRequestDto;
import com.vozniuk.bookstore.dto.order.OrderResponseDto;
import com.vozniuk.bookstore.dto.order.OrderUpdateRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto save(OrderRequestDto requestDto);

    List<OrderResponseDto> findAll(Pageable pageable);

    OrderResponseDto updateOrderStatus(Long id, OrderUpdateRequestDto requestDto);

    List<OrderItemDto> findAllOrderItems(Long orderId, Pageable pageable);

    OrderItemDto findOrderItemById(Long orderId, Long itemId);
}
