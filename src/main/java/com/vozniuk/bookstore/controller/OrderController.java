package com.vozniuk.bookstore.controller;

import com.vozniuk.bookstore.dto.order.OrderItemDto;
import com.vozniuk.bookstore.dto.order.OrderRequestDto;
import com.vozniuk.bookstore.dto.order.OrderResponseDto;
import com.vozniuk.bookstore.dto.order.OrderUpdateRequestDto;
import com.vozniuk.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place an order",
            description = "Place an order")
    public OrderResponseDto placeOrder(@RequestBody @Valid OrderRequestDto requestDto) {
        return orderService.save(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all orders",
            description = "Get a list of all orders made by user")
    public List<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderService.findAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update order status",
            description = "Update order status")
    public OrderResponseDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody @Valid OrderUpdateRequestDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all order items",
            description = "Get a list of all items in the order")
    public List<OrderItemDto> getAllOrderItems(@PathVariable Long orderId, Pageable pageable) {
        return orderService.findAllOrderItems(orderId, pageable);
    }

    @GetMapping("{orderId}/items/{itemId}")
    @Operation(summary = "Get order item by id",
            description = "Get order item by order id")
    public OrderItemDto getAllOrderItems(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.findOrderItemById(orderId, itemId);
    }
}
