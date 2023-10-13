package com.vozniuk.bookstore.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShoppingCartRequestDto {
    @NotNull
    private Long bookId;
    @Min(1)
    private int quantity;
}
