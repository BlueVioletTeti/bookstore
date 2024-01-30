package com.vozniuk.bookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.ISBN;

@Data
@Accessors(chain = true)
public class CreateBookRequestDto {
    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String title;
    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String author;
    @NotNull
    @NotEmpty
    @ISBN
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
    @NotNull
    private Set<Long> categoryIds;
}
