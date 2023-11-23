package com.vozniuk.bookstore.repository;

import static com.vozniuk.bookstore.model.Role.RoleName.ROLE_USER;

import com.vozniuk.bookstore.model.Book;
import com.vozniuk.bookstore.model.CartItem;
import com.vozniuk.bookstore.model.Role;
import com.vozniuk.bookstore.model.ShoppingCart;
import com.vozniuk.bookstore.model.User;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@DataJpaTest
class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Verify findByUserId() method works correctly")
    @Sql(scripts = {
            "classpath:database/carts/add-book-to-books-table.sql",
            "classpath:database/carts/add-user-to-users-table.sql",
            "classpath:database/carts/add-shopping_cart-to-shopping_carts-table.sql",
            "classpath:database/carts/add-cart_item-to-cart_items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/carts/remove-all-from-all_tables.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_ValidData_ReturnsShoppingCart() {
        Long id = 1L;
        Book book = new Book()
                .setId(id)
                .setTitle("Java Book")
                .setAuthor("Some author")
                .setIsbn("9781617290459")
                .setPrice(BigDecimal.valueOf(499));
        User user = new User()
                .setId(2L)
                .setEmail("user@example.com")
                .setPassword("password")
                .setFirstName("User")
                .setLastName("Userson")
                .setRoles(Set.of(new Role().setId(2L).setName(ROLE_USER)));
        CartItem cartItem = new CartItem()
                .setId(id)
                .setBook(book)
                .setQuantity(1);
        ShoppingCart shoppingCart = new ShoppingCart()
                .setId(id)
                .setUser(user)
                .setCartItems(new HashSet<>(Set.of(cartItem)));

        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(id);
        EqualsBuilder.reflectionEquals(shoppingCart, actual);
    }
}
