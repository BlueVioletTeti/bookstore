package com.vozniuk.bookstore.repository;

import com.vozniuk.bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("FROM ShoppingCart sc "
            + "LEFT JOIN FETCH sc.cartItems ci "
            + "LEFT JOIN FETCH ci.book "
            + "LEFT JOIN FETCH sc.user u WHERE u.id = :id ")
    Optional<ShoppingCart> findByUserId(Long id);
}
