package com.shubham.event_manager.controller;

import com.shubham.event_manager.dto.ChangePasswordRequest;
import com.shubham.event_manager.dto.UserProfileDTO;
import com.shubham.event_manager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Profile",
        description = "View and manage your own profile")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(
            summary = "Get my profile",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<UserProfileDTO> getMyProfile(
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                userService.getMyProfile(
                        userDetails.getUsername()));
    }

    @PutMapping("/me")
    @Operation(
            summary = "Update my profile",
            description = "Update name, bio, phone, city. " +
                    "Email and role cannot be changed here.",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<UserProfileDTO> updateMyProfile(
            @RequestBody UserProfileDTO dto,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                userService.updateMyProfile(
                        userDetails.getUsername(), dto));
    }

    @PutMapping("/me/password")
    @Operation(
            summary = "Change my password",
            description = "Requires current password verification.",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody
            ChangePasswordRequest request,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        userService.changePassword(
                userDetails.getUsername(), request);
        return ResponseEntity.ok(
                "Password changed successfully");
    }

    @DeleteMapping("/me")
    @Operation(
            summary = "Delete my account",
            description = "Permanently deletes your account. " +
                    "This cannot be undone.",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<String> deleteMyAccount(
            @AuthenticationPrincipal
            UserDetails userDetails) {
        userService.deleteMyAccount(
                userDetails.getUsername());
        return ResponseEntity.ok(
                "Account deleted successfully");
    }
}