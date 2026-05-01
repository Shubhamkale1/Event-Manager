package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.AuthResponse;
import com.shubham.event_manager.dto.LoginRequest;
import com.shubham.event_manager.dto.RegisterRequest;

public interface AuthService {
     AuthResponse register(RegisterRequest request);
     AuthResponse login(LoginRequest request);
}
