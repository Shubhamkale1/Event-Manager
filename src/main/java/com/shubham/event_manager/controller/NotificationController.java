package com.shubham.event_manager.controller;

import com.shubham.event_manager.dto.NotificationDTO;
import com.shubham.event_manager.service.NotificationService;
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
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications",
        description = "User notification management")
public class NotificationController {

    private final NotificationService
            notificationService;

    @GetMapping
    @Operation(
            summary = "Get all my notifications",
            description = "Returns all notifications " +
                    "ordered by newest first",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<List<NotificationDTO>>
    getAll(@AuthenticationPrincipal
           UserDetails userDetails) {
        return ResponseEntity.ok(
                notificationService.getMyNotifications(
                        userDetails.getUsername()));
    }

    @GetMapping("/unread")
    @Operation(
            summary = "Get unread notifications",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<List<NotificationDTO>>
    getUnread(@AuthenticationPrincipal
              UserDetails userDetails) {
        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(
                        userDetails.getUsername()));
    }

    @GetMapping("/unread-count")
    @Operation(
            summary = "Get unread notification count",
            description = "Lightweight endpoint for " +
                    "notification badge count",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<Long> getUnreadCount(
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                notificationService.getUnreadCount(
                        userDetails.getUsername()));
    }

    @PutMapping("/{id}/read")
    @Operation(
            summary = "Mark notification as read",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<String> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        notificationService.markAsRead(
                id, userDetails.getUsername());
        return ResponseEntity.ok("Marked as read");
    }

    @PutMapping("/read-all")
    @Operation(
            summary = "Mark all notifications as read",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<String> markAllAsRead(
            @AuthenticationPrincipal
            UserDetails userDetails) {
        notificationService.markAllAsRead(
                userDetails.getUsername());
        return ResponseEntity.ok(
                "All notifications marked as read");
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a notification",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        notificationService.deleteNotification(
                id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}