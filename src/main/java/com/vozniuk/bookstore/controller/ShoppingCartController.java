package com.vozniuk.bookstore.controller;

import com.vozniuk.bookstore.dto.cart.CartItemDto;
import com.vozniuk.bookstore.dto.cart.ShoppingCartRequestDto;
import com.vozniuk.bookstore.dto.cart.ShoppingCartRequestDtoWithoutCartItemId;
import com.vozniuk.bookstore.dto.cart.ShoppingCartResponseDto;
import com.vozniuk.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get user's shopping cart",
            description = "Get a shopping cart by user id")
    public ShoppingCartResponseDto getCart() {
        return shoppingCartService.getCart();
    }

    @PostMapping
    @Operation(summary = "Add a book to the shopping cart",
            description = "Add a book to the shopping cart")
    public ShoppingCartResponseDto addItem(@RequestBody @Valid ShoppingCartRequestDto requestDto) {
        return shoppingCartService.addToCart(requestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update quantity of an item",
            description = "Update quantity of a book in the shopping cart")
    public CartItemDto updateItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody @Valid ShoppingCartRequestDtoWithoutCartItemId requestDto) {
        return shoppingCartService.updateCartItem(cartItemId, requestDto);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete an item", description = "Remove a book from the shopping cart")
    public void deleteItem(@PathVariable Long cartItemId) {
        shoppingCartService.removeCartItem(cartItemId);
    }
}
