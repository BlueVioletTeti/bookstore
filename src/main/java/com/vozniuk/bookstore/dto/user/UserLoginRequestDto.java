package com.vozniuk.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotNull
    @Size(min = 8, max = 20)
    @Email
    private String email;
    @NotNull
    @Size(min = 8, max = 20)
    private String password;
}
