package com.vozniuk.bookstore.service.impl;

import com.vozniuk.bookstore.dto.cart.CartItemDto;
import com.vozniuk.bookstore.dto.cart.ShoppingCartRequestDto;
import com.vozniuk.bookstore.dto.cart.ShoppingCartRequestDtoWithoutCartItemId;
import com.vozniuk.bookstore.dto.cart.ShoppingCartResponseDto;
import com.vozniuk.bookstore.exception.EntityNotFoundException;
import com.vozniuk.bookstore.mapper.CartItemMapper;
import com.vozniuk.bookstore.mapper.ShoppingCartMapper;
import com.vozniuk.bookstore.model.Book;
import com.vozniuk.bookstore.model.CartItem;
import com.vozniuk.bookstore.model.ShoppingCart;
import com.vozniuk.bookstore.model.User;
import com.vozniuk.bookstore.repository.BookRepository;
import com.vozniuk.bookstore.repository.CartItemRepository;
import com.vozniuk.bookstore.repository.ShoppingCartRepository;
import com.vozniuk.bookstore.repository.UserRepository;
import com.vozniuk.bookstore.security.AuthenticationService;
import com.vozniuk.bookstore.service.ShoppingCartService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final AuthenticationService authenticationService;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartResponseDto getCartDto() {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(authenticationService.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart by user with id: "
                        + authenticationService.getUserId()));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional(readOnly = true)
    public ShoppingCart getCart() {
        return shoppingCartRepository
                .findByUserId(authenticationService.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart by user with id: "
                                + authenticationService.getUserId()));
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addToCart(ShoppingCartRequestDto requestDto) {
        User user = userRepository.findById(authenticationService.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(authenticationService.getUserId()).orElseGet(() -> {
                    ShoppingCart newShoppingCart = new ShoppingCart();
                    newShoppingCart.setUser(user);
                    shoppingCartRepository.save(newShoppingCart);
                    return newShoppingCart;
                });

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        cartItems.add(cartItem);
        shoppingCartRepository.save(shoppingCart);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public CartItemDto updateCartItem(
            Long cartItemId, ShoppingCartRequestDtoWithoutCartItemId requestDto) {
        ShoppingCart shoppingCart = getCart();
        CartItem cartItem = getCartItem(cartItemId, shoppingCart);
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto removeCartItem(Long cartItemId) {
        ShoppingCart shoppingCart = getCart();
        CartItem cartItem = getCartItem(cartItemId, shoppingCart);
        cartItemRepository.delete(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public void clear() {
        ShoppingCart shoppingCart = getCart();
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        cartItemRepository.deleteAll(cartItems);
    }

    private CartItem getCartItem(Long cartItemId, ShoppingCart shoppingCart) {
        return cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item by id %s for user with id %s"
                                .formatted(cartItemId, authenticationService.getUserId()))
                );
    }
}
