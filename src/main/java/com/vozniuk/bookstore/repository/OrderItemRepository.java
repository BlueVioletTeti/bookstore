package com.vozniuk.bookstore.repository;

import com.vozniuk.bookstore.model.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.order o "
            + "WHERE oi.id = :itemId AND o.id = :orderId")
    Optional<OrderItem> findByOrderAndItemIds(@Param("orderId")Long orderId,
                                              @Param("itemId") Long itemId);
}
