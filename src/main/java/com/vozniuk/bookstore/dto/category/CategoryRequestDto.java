package com.vozniuk.bookstore.dto.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String name;
    @Size(max = 255)
    private String description;
}
