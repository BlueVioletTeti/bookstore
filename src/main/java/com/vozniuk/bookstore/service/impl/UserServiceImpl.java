package com.vozniuk.bookstore.service.impl;

import com.vozniuk.bookstore.dto.user.UserRegistrationRequest;
import com.vozniuk.bookstore.dto.user.UserResponseDto;
import com.vozniuk.bookstore.exception.EntityNotFoundException;
import com.vozniuk.bookstore.exception.RegistrationException;
import com.vozniuk.bookstore.mapper.UserMapper;
import com.vozniuk.bookstore.model.Role;
import com.vozniuk.bookstore.model.User;
import com.vozniuk.bookstore.repository.RoleRepository;
import com.vozniuk.bookstore.repository.UserRepository;
import com.vozniuk.bookstore.security.AuthenticationService;
import com.vozniuk.bookstore.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final AuthenticationService authenticationService;

    @Transactional
    @Override
    public UserResponseDto register(UserRegistrationRequest request) throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.getByName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(role));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    @Override
    public User getCurrentUser() {
        return userRepository.findById(authenticationService.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find user with id: "
                        + authenticationService.getUserId()));
    }
}
