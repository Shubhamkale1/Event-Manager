package com.shubham.event_manager.controller;

import com.shubham.event_manager.dto.DashboardDTO;
import com.shubham.event_manager.dto.EventAnalyticsDTO;
import com.shubham.event_manager.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard",
        description = "Analytics and insights")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/organizer")
    @Operation(
            summary = "Get organizer dashboard",
            description = "Stats for all your events",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<DashboardDTO>
    getOrganizerDashboard(
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                dashboardService.getOrganizerDashboard(
                        userDetails.getUsername()));
    }

    @GetMapping("/admin")
    @Operation(
            summary = "Get platform-wide stats",
            description = "Admin only",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<Map<String, Object>>
    getAdminDashboard() {
        return ResponseEntity.ok(
                dashboardService.getAdminDashboard());
    }

    @GetMapping("/events/{id}/analytics")
    @Operation(
            summary = "Get detailed event analytics",
            description = "Organizer or admin only",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<EventAnalyticsDTO>
    getEventAnalytics(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                dashboardService.getEventAnalytics(
                        id, userDetails.getUsername()));
    }
}