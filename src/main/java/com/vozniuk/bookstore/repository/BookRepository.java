package com.vozniuk.bookstore.repository;

import com.vozniuk.bookstore.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByCategoriesId(@Param("categoryId") Long categoryId);
}
