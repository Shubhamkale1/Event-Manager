package com.shubham.event_manager.service;


import com.shubham.event_manager.dto.AuthResponse;
import com.shubham.event_manager.dto.LoginRequest;
import com.shubham.event_manager.dto.RegisterRequest;
import com.shubham.event_manager.entity.User;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.repository.UserRepository;
import com.shubham.event_manager.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
       public AuthResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException(
                    "Email already resistered: " + request.getEmail());
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        userRepository.save(user);
        log.info("New user registered: {}", request.getEmail());

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token,user.getEmail(),
                user.getName(), user.getRole());
    }
    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + request.getEmail()));

        String token = jwtUtil.generateToken(user);
        log.info("User logged in: {}", request.getEmail());

        return new AuthResponse(token, user.getEmail(),
                user.getName(), user.getRole());
    }
}
