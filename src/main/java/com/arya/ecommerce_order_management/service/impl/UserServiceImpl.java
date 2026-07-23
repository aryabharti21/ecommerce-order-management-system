package com.arya.ecommerce_order_management.service.impl;

import com.arya.ecommerce_order_management.dto.request.CreateAddressRequest;
import com.arya.ecommerce_order_management.dto.request.CreateUserRequest;
import com.arya.ecommerce_order_management.dto.response.AddressResponse;
import com.arya.ecommerce_order_management.dto.response.UserResponse;
import com.arya.ecommerce_order_management.entity.Address;
import com.arya.ecommerce_order_management.entity.User;
import com.arya.ecommerce_order_management.entity.UserAddress;
import com.arya.ecommerce_order_management.entity.UserAddressId;
import com.arya.ecommerce_order_management.exception.DuplicateResourceException;
import com.arya.ecommerce_order_management.exception.ResourceNotFoundException;
import com.arya.ecommerce_order_management.repository.AddressRepository;
import com.arya.ecommerce_order_management.repository.UserAddressRepository;
import com.arya.ecommerce_order_management.repository.UserRepository;
import com.arya.ecommerce_order_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserAddressRepository userAddressRepository;

    @Override
    @Transactional
    public UserResponse registerUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("User", "email",  request.email());
        }
        if (userRepository.existsByPhone(request.phone())) {
            throw new DuplicateResourceException("User", "phone",  request.phone());
        }

        User newUser = User.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .build();

        User savedUser = userRepository.save(newUser);

        return UserResponse.from(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return UserResponse.from(user);
    }

    @Override
    @Transactional
    public AddressResponse addAddressToUser(Long userId, CreateAddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Address address = Address.builder()
                .country(request.country())
                .state(request.state())
                .district(request.district())
                .line1(request.line1())
                .line2(request.line2())
                .zipcode(request.zipcode())
                .build();

        Address savedAddress = addressRepository.save(address);

        UserAddressId userAddressId = new UserAddressId(userId, savedAddress.getId());

        UserAddress userAddress = UserAddress.builder()
                .id(userAddressId)
                .user(user)
                .address(savedAddress)
                .build();

        userAddressRepository.save(userAddress);

        return AddressResponse.from(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getUserAddresses(Long id) {
        if (!userRepository.existsById(id)) throw new ResourceNotFoundException("User", "id", id);

        return userAddressRepository.findByUserId(id)
                .stream()
                .map(UserAddress::getAddress)
                .map(AddressResponse::from)
                .toList();
    }
}
