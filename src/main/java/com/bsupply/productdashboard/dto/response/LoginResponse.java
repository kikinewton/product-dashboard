package com.bsupply.productdashboard.dto.response;

public record LoginResponse(String token, long expiresIn, UserResponse userResponse) {
}
