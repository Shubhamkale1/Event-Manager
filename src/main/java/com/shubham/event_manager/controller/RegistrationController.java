package com.shubham.event_manager.controller;

import com.shubham.event_manager.dto.RegistrationDTO;
import com.shubham.event_manager.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Event Registration",
        description = "Register and manage event attendance")
public class RegistrationController {

    private final RegistrationService
            registrationService;

    @PostMapping("/{id}/register")
    @Operation(
            summary = "Register for an event",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<RegistrationDTO> register(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                registrationService.registerForEvent(
                        id, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}/register")
    @Operation(
            summary = "Cancel my registration",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<String>
    cancelRegistration(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        registrationService.cancelRegistration(
                id, userDetails.getUsername());
        return ResponseEntity.ok(
                "Registration cancelled");
    }

    @GetMapping("/my-registrations")
    @Operation(
            summary = "Get my event registrations",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<List<RegistrationDTO>>
    getMyRegistrations(
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                registrationService.getMyRegistrations(
                        userDetails.getUsername()));
    }

    @GetMapping("/{id}/registrations")
    @Operation(
            summary = "Get event attendees",
            description = "Organizer or admin only",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<List<RegistrationDTO>>
    getEventRegistrations(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                registrationService.getEventRegistrations(
                        id, userDetails.getUsername()));
    }

    @GetMapping("/{id}/registration-status")
    @Operation(
            summary = "Check if I am registered",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<Boolean>
    checkRegistrationStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                registrationService.isUserRegistered(
                        id, userDetails.getUsername()));
    }

    @GetMapping("/{id}/spots-remaining")
    @Operation(
            summary = "Get remaining spots",
            description = "Public endpoint"
    )
    public ResponseEntity<Long>
    getSpotsRemaining(
            @PathVariable Long id) {
        long confirmed =
                registrationService
                        .getConfirmedCount(id);
        return ResponseEntity.ok(confirmed);
    }
}