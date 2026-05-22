package com.shubham.event_manager.controller;

import com.shubham.event_manager.dto.WaitlistDTO;
import com.shubham.event_manager.service.WaitlistService;
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
@Tag(name = "Waitlist",
        description = "Join waitlist for full events")
public class WaitlistController {

    private final WaitlistService waitlistService;

    @PostMapping("/{id}/waitlist")
    @Operation(
            summary = "Join waitlist for a full event",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<WaitlistDTO> joinWaitlist(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                waitlistService.joinWaitlist(
                        id, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}/waitlist")
    @Operation(
            summary = "Leave the waitlist",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<String> leaveWaitlist(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        waitlistService.leaveWaitlist(
                id, userDetails.getUsername());
        return ResponseEntity.ok("Left waitlist");
    }

    @GetMapping("/{id}/waitlist")
    @Operation(
            summary = "Get waitlist for an event",
            description = "Organizer or admin only",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<List<WaitlistDTO>>
    getWaitlist(@PathVariable Long id) {
        return ResponseEntity.ok(
                waitlistService.getEventWaitlist(id));
    }

    @GetMapping("/my-waitlist")
    @Operation(
            summary = "Events I am waitlisted for",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<List<WaitlistDTO>>
    getMyWaitlist(
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                waitlistService.getMyWaitlist(
                        userDetails.getUsername()));
    }
}