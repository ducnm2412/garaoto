package com.example.garaoto.service;

import com.example.garaoto.dto.request.LoginRequest;
import com.example.garaoto.dto.request.RegisterRequest;
import com.example.garaoto.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}