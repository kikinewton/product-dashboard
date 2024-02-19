package com.bsupply.productdashboard.service;

import com.bsupply.productdashboard.dto.response.UserResponse;
import com.bsupply.productdashboard.factory.UserResponseFactory;
import com.bsupply.productdashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    @Cacheable(value = "users")
    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> UserResponseFactory.getUserResponse(u))
                .collect(Collectors.toList());
    }
}
