package com.vozniuk.bookstore.repository;

import com.vozniuk.bookstore.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    @Query("SELECT DISTINCT b FROM Book b "
            + "LEFT JOIN FETCH b.categories")
    Page<Book> findAll(Pageable pageable);

    @Override
    @Query("SELECT DISTINCT b FROM Book b "
            + "LEFT JOIN FETCH b.categories WHERE b.id = :categoryId")
    Optional<Book> findById(@Param("categoryId") Long categoryId);

    @Query("SELECT DISTINCT b FROM Book b "
            + "LEFT JOIN FETCH b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(@Param("categoryId") Long categoryId);
}
