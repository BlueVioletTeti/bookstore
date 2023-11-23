package com.vozniuk.bookstore.service.impl;

import static com.vozniuk.bookstore.model.Role.RoleName.ROLE_USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.vozniuk.bookstore.dto.cart.CartItemDto;
import com.vozniuk.bookstore.dto.cart.ShoppingCartRequestDto;
import com.vozniuk.bookstore.dto.cart.ShoppingCartRequestDtoWithoutCartItemId;
import com.vozniuk.bookstore.dto.cart.ShoppingCartResponseDto;
import com.vozniuk.bookstore.mapper.CartItemMapper;
import com.vozniuk.bookstore.mapper.ShoppingCartMapper;
import com.vozniuk.bookstore.model.Book;
import com.vozniuk.bookstore.model.CartItem;
import com.vozniuk.bookstore.model.Role;
import com.vozniuk.bookstore.model.ShoppingCart;
import com.vozniuk.bookstore.model.User;
import com.vozniuk.bookstore.repository.BookRepository;
import com.vozniuk.bookstore.repository.CartItemRepository;
import com.vozniuk.bookstore.repository.ShoppingCartRepository;
import com.vozniuk.bookstore.repository.UserRepository;
import com.vozniuk.bookstore.security.AuthenticationService;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    private static final Long DEFAULT_ID = 1L;

    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    //    @Test
    //    @DisplayName("Verify getCart() method works correctly")
    //    void getCart_RepositoryNotEmpty_ReturnsShoppingCartResponseDto() {
    //        User user = getUser();
    //        Book book = getBook();
    //        CartItem cartItem = getCartItem(book);
    //        ShoppingCart shoppingCart = getShoppingCart(cartItem, user);
    //        CartItemDto cartItemDto = getCartItemDto(book);
    //        ShoppingCartResponseDto shoppingCartResponseDto =
    //        getShoppingCartResponseDto(cartItemDto);
    //
    //        when(shoppingCartRepository.findByUserId(any(Long.class)))
    //                .thenReturn(Optional.ofNullable(shoppingCart));
    //        when(authenticationService.getUserId()).thenReturn(DEFAULT_ID);
    //        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartResponseDto);
    //
    //        ShoppingCartResponseDto expected = shoppingCartService.getCart();
    //        assertThat(expected).isEqualTo(shoppingCartResponseDto);
    //        verifyNoMoreInteractions(shoppingCartRepository,
    //        authenticationService, shoppingCartMapper);
    //    }

    @Test
    @DisplayName("Verify addToCart() method works correctly")
    void addToCart_ValidRequestDto_ReturnsShoppingCartResponseDto() {
        User user = getUser();
        Book book = getBook();
        CartItem cartItem = getCartItem(book);
        ShoppingCart shoppingCart = getShoppingCart(cartItem, user);
        CartItemDto cartItemDto = getCartItemDto(book);
        ShoppingCartRequestDto requestDto = new ShoppingCartRequestDto()
                .setBookId(DEFAULT_ID)
                .setQuantity(1);
        ShoppingCartResponseDto responseDto = getShoppingCartResponseDto(cartItemDto);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(authenticationService.getUserId()).thenReturn(DEFAULT_ID);
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUserId(any(Long.class)))
                .thenReturn(Optional.ofNullable(shoppingCart));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(shoppingCartMapper.toDto(any(ShoppingCart.class))).thenReturn(responseDto);

        ShoppingCartResponseDto expected = shoppingCartService.addToCart(requestDto);

        assertThat(expected).isEqualTo(responseDto);
        verifyNoMoreInteractions(userRepository, shoppingCartRepository, authenticationService,
                bookRepository, cartItemRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("Verify updateCartItem() method works correctly")
    void updateCartItem_ValidIdAndRequestDto_ReturnsCartItemDto() {
        User user = getUser();
        Book book = getBook();
        CartItem cartItem = getCartItem(book);
        ShoppingCart shoppingCart = getShoppingCart(cartItem, user);
        CartItemDto cartItemDto = getCartItemDto(book);
        ShoppingCartRequestDtoWithoutCartItemId requestDto =
                new ShoppingCartRequestDtoWithoutCartItemId().setQuantity(2);

        when(shoppingCartRepository.findByUserId(any(Long.class)))
                .thenReturn(Optional.ofNullable(shoppingCart));
        when(authenticationService.getUserId()).thenReturn(DEFAULT_ID);
        when(cartItemRepository.findByIdAndShoppingCartId(DEFAULT_ID, DEFAULT_ID))
                .thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDto);

        CartItemDto expected = shoppingCartService.updateCartItem(DEFAULT_ID, requestDto);

        assertThat(expected).isEqualTo(cartItemDto);
        verifyNoMoreInteractions(shoppingCartRepository, authenticationService,
                cartItemRepository, cartItemMapper);
    }

    @Test
    @DisplayName("Verify removeCartItem() method works correctly")
    void removeCartItem_ValidId_ReturnsShoppingCartResponseDto() {
        Book book = getBook();
        CartItem cartItem = getCartItem(book);
        User user = getUser();
        ShoppingCart shoppingCart = getShoppingCart(cartItem, user);
        CartItemDto cartItemDto = getCartItemDto(book);
        ShoppingCartResponseDto responseDto = getShoppingCartResponseDto(cartItemDto);

        when(shoppingCartRepository.findByUserId(any(Long.class)))
                .thenReturn(Optional.ofNullable(shoppingCart));
        when(authenticationService.getUserId()).thenReturn(DEFAULT_ID);
        when(cartItemRepository.findByIdAndShoppingCartId(DEFAULT_ID, DEFAULT_ID))
                .thenReturn(Optional.of(cartItem));
        when(shoppingCartMapper.toDto(any(ShoppingCart.class))).thenReturn(responseDto);

        shoppingCartService.removeCartItem(DEFAULT_ID);
        verify(cartItemRepository).delete(cartItem);
        verifyNoMoreInteractions(shoppingCartRepository, authenticationService,
                cartItemRepository, shoppingCartMapper);
    }

    private static Book getBook() {
        Book book = new Book()
                .setId(DEFAULT_ID)
                .setTitle("Java Book")
                .setAuthor("Some author")
                .setIsbn("9781617290459")
                .setPrice(BigDecimal.valueOf(499));
        return book;
    }

    private static User getUser() {
        User user = new User()
                .setId(DEFAULT_ID)
                .setEmail("user@example.com")
                .setPassword("password")
                .setFirstName("User")
                .setLastName("Userson")
                .setRoles(Set.of(new Role().setId(DEFAULT_ID).setName(ROLE_USER)));
        return user;
    }

    private static CartItem getCartItem(Book book) {
        CartItem cartItem = new CartItem()
                .setId(DEFAULT_ID)
                .setBook(book)
                .setQuantity(1);
        return cartItem;
    }

    private static CartItemDto getCartItemDto(Book book) {
        CartItemDto cartItemDto = new CartItemDto()
                .setId(DEFAULT_ID)
                .setBookId(DEFAULT_ID)
                .setBookTitle(book.getTitle())
                .setQuantity(1);
        return cartItemDto;
    }

    private static ShoppingCart getShoppingCart(CartItem cartItem, User user) {
        ShoppingCart shoppingCart = new ShoppingCart()
                .setId(DEFAULT_ID)
                .setUser(user)
                .setCartItems(new HashSet<>(Set.of(cartItem)));
        return shoppingCart;
    }

    private static ShoppingCartResponseDto getShoppingCartResponseDto(CartItemDto cartItemDto) {
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto()
                .setId(DEFAULT_ID)
                .setUserId(DEFAULT_ID)
                .setCartItems(Set.of(cartItemDto));
        return shoppingCartResponseDto;
    }
}
