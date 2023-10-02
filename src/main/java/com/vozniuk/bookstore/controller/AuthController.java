package com.vozniuk.bookstore.controller;

import com.vozniuk.bookstore.dto.user.UserLoginRequestDto;
import com.vozniuk.bookstore.dto.user.UserLoginResponseDto;
import com.vozniuk.bookstore.dto.user.UserRegistrationRequest;
import com.vozniuk.bookstore.dto.user.UserResponseDto;
import com.vozniuk.bookstore.exception.RegistrationException;
import com.vozniuk.bookstore.security.AuthenticationService;
import com.vozniuk.bookstore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequest request)
            throws RegistrationException {
        return userService.register(request);

    }
}
