package com.example.student_management.service;

import com.example.student_management.dto.AuthResponse;
import com.example.student_management.dto.LoginRequest;
import com.example.student_management.dto.RefreshRequest;
import com.example.student_management.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshRequest request);
}