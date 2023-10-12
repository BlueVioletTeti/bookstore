package com.vozniuk.bookstore.dto.order;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Set<OrderItemDto> orderItems;
    private LocalDateTime orderDate;
    private BigInteger total;
    private String status;
}
