package com.bsupply.productdashboard.service;

import com.bsupply.productdashboard.dto.request.UpdateUserRequest;
import com.bsupply.productdashboard.dto.request.UserRegistrationRequest;
import com.bsupply.productdashboard.dto.response.UserResponse;
import com.bsupply.productdashboard.entity.User;
import com.bsupply.productdashboard.exception.UserNotFoundException;
import com.bsupply.productdashboard.exception.UserWithEmailExistException;
import com.bsupply.productdashboard.factory.UserResponseFactory;
import com.bsupply.productdashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "users")
    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> UserResponseFactory.getUserResponse(u))
                .collect(Collectors.toList());
    }

    @CacheEvict(value = {"users", "userById", "userByEmail"})
    public void updateUser(UUID userId, UpdateUserRequest updateUserRequest) {

        log.info("Update user with id {}", userId);
        User user = getUser(userId);

        if (!updateUserRequest.firstName().isBlank()) {
            user.setFirstName(updateUserRequest.firstName());
        }
        if (!updateUserRequest.lastName().isBlank()) {
            user.setLastName(updateUserRequest.lastName());
        }
        if (!updateUserRequest.email().isBlank()) {
            user.setEmail(updateUserRequest.email());
        }
        if (updateUserRequest.role() != null || updateUserRequest.role() != user.getRole())  {
            user.setRole(updateUserRequest.role());
        }
        userRepository.save(user);
    }

    @Cacheable(value = "userById")
    private User getUser(UUID userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));
    }

    public void deleteUser(UUID userId) {

        log.info("Delete user with id {}", userId);
        userRepository.deleteById(userId);
    }

    public UserResponse getUserById(UUID userId) {

        log.info("Fetch user with id {}", userId);
        return UserResponseFactory.getUserResponse(getUser(userId));
    }

    @Cacheable(value = "userByEmail")
    public UserResponse getUserByEmail(String email) {

        log.info("Fetch user by email {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return UserResponseFactory.getUserResponse(user);
    }

    @CacheEvict(value = {"users", "userById", "userByEmail"})
    public void disableUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        log.info("Disable user {}", user.getEmail());
        user.setEnabled(false);
        userRepository.save(user);
    }

    @CacheEvict(value = {"users", "userById", "userByEmail"})
    public UserResponse addUser(UserRegistrationRequest userRegistrationRequest) {

        checkUserWithEmailExist(userRegistrationRequest.email());
        log.info("Add new user {}", userRegistrationRequest);
        User user = new User();
        user.setEmail(userRegistrationRequest.email());
        user.setFirstName(userRegistrationRequest.firstName());
        user.setLastName(userRegistrationRequest.lastName());
        user.setPassword(passwordEncoder.encode("P@ssword1.com"));
        user.setRole(userRegistrationRequest.role());
        User savedUser = userRepository.save(user);
        return UserResponseFactory.getUserResponse(savedUser);
    }

    private void checkUserWithEmailExist(String email) {

        log.info("Check if user {} exists", email);
        if (userRepository.existsByEmail(email)) {
            throw new UserWithEmailExistException(email);
        }
    }

}
