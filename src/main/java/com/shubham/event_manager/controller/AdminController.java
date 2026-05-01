package com.shubham.event_manager.controller;


import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin only operation")
public class AdminController {
    private final UserRepository userRepository;

    @PutMapping("/users/{email}/promote")
    @Operation(
            summary = "promote user to ADMIN",
            description = "only existing ADMINs can call this",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<String> promoteToAdmin(@PathVariable String email){
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not Found:" + email));

        user.setRole("ADMIN");
        userRepository.save(user);

        return ResponseEntity.ok(email + "has been promoted to ADMIN");
    }

    @PutMapping("/users/{email}/demote")
    @Operation(
            summary = "Demote ADMIN to USER",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<String> demoteToAdmin(@PathVariable String email){
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not Found:" + email));

        user.setRole("USER");
        userRepository.save(user);

        return ResponseEntity.ok(email + "has been demoted to USER");
    }

    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userRepository.findAll()
                .stream()
                .map(u -> u.getEmail() + " _ " + u.getRole())
                .toList());
    }
}
