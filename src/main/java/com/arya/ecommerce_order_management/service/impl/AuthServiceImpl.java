package com.arya.ecommerce_order_management.service.impl;

import com.arya.ecommerce_order_management.dto.request.LoginRequest;
import com.arya.ecommerce_order_management.dto.request.RegisterRequest;
import com.arya.ecommerce_order_management.dto.response.LoginResponse;
import com.arya.ecommerce_order_management.entity.User;
import com.arya.ecommerce_order_management.entity.enums.Role;
import com.arya.ecommerce_order_management.exception.DuplicateResourceException;
import com.arya.ecommerce_order_management.exception.ResourceNotFoundException;
import com.arya.ecommerce_order_management.repository.UserRepository;
import com.arya.ecommerce_order_management.service.AuthService;
import com.arya.ecommerce_order_management.config.JwtService;
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
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) throw new DuplicateResourceException("User", "email", request.email());
        if (userRepository.existsByPhone(request.phone())) throw new DuplicateResourceException("User", "phone", request.phone());
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);
        return LoginResponse.from(token, savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(user);

        return LoginResponse.from(token,user);
    }
}
