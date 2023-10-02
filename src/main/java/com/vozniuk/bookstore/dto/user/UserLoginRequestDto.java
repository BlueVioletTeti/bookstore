package com.vozniuk.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotEmpty
    @NotBlank
    @Size(min = 8, max = 20)
    @Email
    private String email;
    @NotEmpty
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
}
