package com.vozniuk.bookstore.repository;

import com.vozniuk.bookstore.model.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi "
            + " LEFT JOIN FETCH oi.book ")
    Page<Order> findAll(Pageable pageable);

    @Override
    @Query("SELECT o FROM Order o "
            + "LEFT JOIN FETCH o.orderItems oi "
            + "LEFT JOIN FETCH oi.book "
            + "WHERE o.id = :orderId")
    Optional<Order> findById(@Param("orderId")Long orderId);
}
