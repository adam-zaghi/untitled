package com.dailycodework.authservice.service;

import com.dailycodework.authservice.dto.AuthResponse;
import com.dailycodework.authservice.dto.LoginRequest;
import com.dailycodework.authservice.dto.RegisterRequest;
import com.dailycodework.authservice.dto.UserResponse;

public interface AuthService {

    UserResponse getUserByEmail(String email);

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
    UserResponse getUserById(Long id);
    UserResponse updateUserRole(Long id, String role);
}