package com.vozniuk.bookstore.service;

import com.vozniuk.bookstore.dto.user.UserRegistrationRequest;
import com.vozniuk.bookstore.dto.user.UserResponseDto;
import com.vozniuk.bookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequest request) throws RegistrationException;
}
