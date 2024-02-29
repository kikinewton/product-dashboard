package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.dto.request.LoginRequest;
import com.bsupply.productdashboard.dto.response.LoginResponse;
import com.bsupply.productdashboard.entity.User;
import com.bsupply.productdashboard.service.AuthenticationService;
import com.bsupply.productdashboard.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginRequest loginRequest) {

        User authenticatedUser = authenticationService.authenticate(loginRequest);
        LoginResponse loginResponse = jwtService.generateToken(authenticatedUser);
        return ResponseEntity.ok(loginResponse);
    }
}
