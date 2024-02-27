package com.bsupply.productdashboard.service;

import com.bsupply.productdashboard.dto.request.LoginRequest;
import com.bsupply.productdashboard.dto.request.UserRegistrationRequest;
import com.bsupply.productdashboard.dto.response.UserResponse;
import com.bsupply.productdashboard.entity.User;
import com.bsupply.productdashboard.exception.UserWithEmailExistException;
import com.bsupply.productdashboard.factory.UserResponseFactory;
import com.bsupply.productdashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public UserResponse signUp(UserRegistrationRequest userRegistrationRequest) {

        checkUserWithEmailExist(userRegistrationRequest.email());
        log.info("Add new user {}", userRegistrationRequest);
        User user = new User();
        user.setEmail(userRegistrationRequest.email());
        user.setFirstName(userRegistrationRequest.firstName());
        user.setLastName(userRegistrationRequest.lastName());
        user.setPassword(passwordEncoder.encode(userRegistrationRequest.password()));
        user.setRole(userRegistrationRequest.role());
        User savedUser = userRepository.save(user);
        return UserResponseFactory.getUserResponse(savedUser);
    }

    public User authenticate(LoginRequest loginRequest) {

        String email = loginRequest.email();
        log.info("User {} attempting login", email);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        loginRequest.password()));

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        CompletableFuture.runAsync(
                () -> userRepository.updateLastLogin(email)
        );
        return user;
    }

    private void checkUserWithEmailExist(String email) {

        log.info("Check if user {} exists", email);
        if (userRepository.existsByEmail(email)) {
            throw new UserWithEmailExistException(email);
        }
    }
}
