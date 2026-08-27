package com.arya.ecommerce_order_management.service.impl;

import com.arya.ecommerce_order_management.dto.request.LoginRequest;
import com.arya.ecommerce_order_management.dto.request.RegisterRequest;
import com.arya.ecommerce_order_management.dto.response.LoginResponse;
import com.arya.ecommerce_order_management.dto.response.UserResponse;
import com.arya.ecommerce_order_management.entity.User;
import com.arya.ecommerce_order_management.entity.enums.Role;
import com.arya.ecommerce_order_management.exception.DuplicateResourceException;
import com.arya.ecommerce_order_management.exception.ResourceNotFoundException;
import com.arya.ecommerce_order_management.repository.UserRepository;
import com.arya.ecommerce_order_management.service.AuthService;
import com.arya.ecommerce_order_management.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email", request.email()));

        if (!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole().name());

        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
