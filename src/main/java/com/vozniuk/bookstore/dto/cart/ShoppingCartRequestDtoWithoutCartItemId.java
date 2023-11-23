package com.vozniuk.bookstore.dto.cart;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShoppingCartRequestDtoWithoutCartItemId {
    @Min(1)
    private int quantity;
}
