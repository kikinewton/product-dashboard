package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.dto.request.LoginRequest;
import com.bsupply.productdashboard.dto.request.UserRegistrationRequest;
import com.bsupply.productdashboard.dto.response.LoginResponse;
import com.bsupply.productdashboard.dto.response.UserResponse;
import com.bsupply.productdashboard.entity.User;
import com.bsupply.productdashboard.service.AuthenticationService;
import com.bsupply.productdashboard.service.JwtService;
import com.bsupply.productdashboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {

        UserResponse userResponse = authenticationService.signUp(userRegistrationRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginRequest loginRequest) {

        User authenticatedUser = authenticationService.authenticate(loginRequest);
        String generatedToken = jwtService.generateToken(authenticatedUser);
        UserResponse user = userService.getUserByEmail(loginRequest.email());
        LoginResponse loginResponse = new LoginResponse(generatedToken, jwtService.getExpirationTime(), user);
        return ResponseEntity.ok(loginResponse);
    }
}
