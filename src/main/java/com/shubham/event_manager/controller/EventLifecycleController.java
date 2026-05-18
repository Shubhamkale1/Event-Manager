package com.shubham.event_manager.controller;

import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.dto.EventLifecycleRequest;
import com.shubham.event_manager.service.EventLifecycleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Event Lifecycle",
        description = "Publish, cancel, and complete events")
public class EventLifecycleController {

    private final EventLifecycleService
            lifecycleService;

    @PostMapping("/{id}/publish")
    @Operation(
            summary = "Publish a draft event",
            description = "Makes event visible to public. " +
                    "Organizer or admin only.",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<EventDTO> publish(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                lifecycleService.publishEvent(
                        id, userDetails.getUsername()));
    }

    @PostMapping("/{id}/cancel")
    @Operation(
            summary = "Cancel an event",
            description = "Cancels event and notifies " +
                    "all registered users. " +
                    "Organizer or admin only.",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<EventDTO> cancel(
            @PathVariable Long id,
            @RequestBody(required = false)
            EventLifecycleRequest request,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                lifecycleService.cancelEvent(
                        id,
                        userDetails.getUsername(),
                        request));
    }

    @PostMapping("/{id}/complete")
    @Operation(
            summary = "Mark event as completed",
            description = "Marks event as done. " +
                    "Enables reviews. " +
                    "Organizer or admin only.",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<EventDTO> complete(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                lifecycleService.completeEvent(
                        id, userDetails.getUsername()));
    }
}