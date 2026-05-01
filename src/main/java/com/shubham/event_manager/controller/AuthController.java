package com.shubham.event_manager.controller;

import com.shubham.event_manager.dto.AuthResponse;
import com.shubham.event_manager.dto.LoginRequest;
import com.shubham.event_manager.dto.RegisterRequest;
import com.shubham.event_manager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication",
        description = "Register and login to get a JWT token")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user",
            description = "Creates account and returns JWT token")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password",
            description = "Returns JWT token on success")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/oauth2/google/url")
    @Operation(
            summary = "Get Google OAuth2 login URL",
            description = "Redirect your frontend to this URL to trigger Google login"
    )
    public ResponseEntity<String> getGoogleLoginUrl(
            HttpServletRequest request) {
        String baseUrl = request.getScheme()
                + "://" + request.getServerName()
                + ":" + request.getServerPort();
        return ResponseEntity.ok(
                baseUrl + "/oauth2/authorization/google"
        );
    }
}
