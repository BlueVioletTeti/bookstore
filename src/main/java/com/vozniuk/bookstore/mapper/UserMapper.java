package com.vozniuk.bookstore.mapper;

import com.vozniuk.bookstore.config.MapperConfig;
import com.vozniuk.bookstore.dto.user.UserRegistrationRequest;
import com.vozniuk.bookstore.dto.user.UserResponseDto;
import com.vozniuk.bookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toUserResponse(User user);

    User toModel(UserRegistrationRequest request);
}
