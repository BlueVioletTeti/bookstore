package com.vozniuk.bookstore.service.impl;

import com.vozniuk.bookstore.dto.order.OrderItemDto;
import com.vozniuk.bookstore.dto.order.OrderRequestDto;
import com.vozniuk.bookstore.dto.order.OrderResponseDto;
import com.vozniuk.bookstore.dto.order.OrderUpdateRequestDto;
import com.vozniuk.bookstore.exception.EntityNotFoundException;
import com.vozniuk.bookstore.mapper.OrderItemMapper;
import com.vozniuk.bookstore.mapper.OrderMapper;
import com.vozniuk.bookstore.model.CartItem;
import com.vozniuk.bookstore.model.Order;
import com.vozniuk.bookstore.model.OrderItem;
import com.vozniuk.bookstore.model.ShoppingCart;
import com.vozniuk.bookstore.repository.OrderItemRepository;
import com.vozniuk.bookstore.repository.OrderRepository;
import com.vozniuk.bookstore.repository.ShoppingCartRepository;
import com.vozniuk.bookstore.repository.UserRepository;
import com.vozniuk.bookstore.security.AuthenticationService;
import com.vozniuk.bookstore.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final AuthenticationService authenticationService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;

    @Override
    public OrderResponseDto save(OrderRequestDto requestDto) {
        Order order = new Order();
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(authenticationService.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find shopping cart to place "
                        + "an order by user with id: " + authenticationService.getUserId()));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal total = BigDecimal.valueOf(0);
        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(item.getId());
            orderItem.setOrder(order);
            orderItem.setBook(item.getBook());
            orderItem.setPrice(item.getBook().getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItems.add(orderItem);
            total = total.add(item.getBook().getPrice());
        }
        order.setUser(userRepository.findById(authenticationService.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find user with id: "
                + authenticationService.getUserId())));
        order.setOrderItems(orderItems);
        order.setTotal(total);
        order.setStatus(Order.Status.NEW);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long id, OrderUpdateRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order with id: " + id));
        order.setStatus(Order.Status.valueOf(requestDto.getStatus()));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderItemDto> findAllOrderItems(Long orderId, Pageable pageable) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order with id: "
                        + orderId));
        Set<OrderItem> orderItems = order.getOrderItems();
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto findOrderItemById(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByOrderAndItemIds(orderId, itemId)
                .orElseThrow(() -> new EntityNotFoundException(("Can't find order item with id %s "
                        + "for order with id %s").formatted(itemId, orderId))
        );
        return orderItemMapper.toDto(orderItem);
    }
}
