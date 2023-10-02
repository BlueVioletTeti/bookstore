package com.vozniuk.bookstore.repository;

import com.vozniuk.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
