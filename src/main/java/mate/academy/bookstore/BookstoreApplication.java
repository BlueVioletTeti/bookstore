package mate.academy.bookstore;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@SpringBootApplication
public class BookstoreApplication {
    private final BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Book title");
            book.setAuthor("Book author");
            book.setIsbn("unique isbn");
            book.setPrice(BigDecimal.valueOf(700));

            bookService.save(book);

            System.out.println(bookService.findAll());
        };
    }
}
