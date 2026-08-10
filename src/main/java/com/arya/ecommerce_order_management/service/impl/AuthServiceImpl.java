package com.arya.ecommerce_order_management.service.impl;

import com.arya.ecommerce_order_management.dto.request.RegisterRequest;
import com.arya.ecommerce_order_management.dto.response.UserResponse;
import com.arya.ecommerce_order_management.entity.User;
import com.arya.ecommerce_order_management.entity.enums.Role;
import com.arya.ecommerce_order_management.exception.DuplicateResourceException;
import com.arya.ecommerce_order_management.repository.UserRepository;
import com.arya.ecommerce_order_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) throw new DuplicateResourceException("User", "email", request.email());

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        return UserResponse.from(savedUser);
    }
}
