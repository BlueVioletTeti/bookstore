package com.vozniuk.bookstore.repository;

import com.vozniuk.bookstore.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("FROM CartItem ci "
            + "LEFT JOIN FETCH ci.shoppingCart sc "
            + "LEFT JOIN FETCH ci.book "
            + "WHERE ci.id = :cartItemId AND sc.id = :shoppingCartId")
    Optional<CartItem> findByIdAndShoppingCartId(@Param("cartItemId") Long cartItemId,
                                                 @Param("shoppingCartId") Long shoppingCartId);
}
