package com.bsupply.productdashboard.factory;

import com.bsupply.productdashboard.dto.response.UserResponse;
import com.bsupply.productdashboard.entity.User;

public class UserResponseFactory {

    UserResponseFactory() {
    }

    public static UserResponse getUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole());
    }
}
