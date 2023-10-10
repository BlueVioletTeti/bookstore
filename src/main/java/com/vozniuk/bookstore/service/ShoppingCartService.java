package com.vozniuk.bookstore.service;

import com.vozniuk.bookstore.dto.cart.ShoppingCartRequestDto;
import com.vozniuk.bookstore.dto.cart.ShoppingCartRequestDtoWithoutCartItemId;
import com.vozniuk.bookstore.dto.cart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCart();

    ShoppingCartResponseDto addToCart(ShoppingCartRequestDto requestDto);

    ShoppingCartResponseDto updateCartItem(Long cartItemId,
                                           ShoppingCartRequestDtoWithoutCartItemId requestDto);

    ShoppingCartResponseDto removeCartItem(Long cartItemId);
}
