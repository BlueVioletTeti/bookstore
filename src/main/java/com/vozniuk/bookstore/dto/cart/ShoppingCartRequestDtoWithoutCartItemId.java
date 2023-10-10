package com.vozniuk.bookstore.dto.cart;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ShoppingCartRequestDtoWithoutCartItemId {
    @Min(1)
    private int quantity;
}
