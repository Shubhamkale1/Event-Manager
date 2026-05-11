package com.shubham.event_manager.controller;

import com.shubham.event_manager.dto.OrganizationDTO;
import com.shubham.event_manager.dto.OrganizationSummaryDTO;
import com.shubham.event_manager.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
@Tag(name = "Organizations",
        description = "Organization management and following")
public class OrganizationController {

    private final OrganizationService orgService;

    @GetMapping
    @Operation(summary = "Get all organizations")
    public ResponseEntity<List<OrganizationSummaryDTO>>
    getAll() {
        return ResponseEntity.ok(
                orgService.getAllOrganizations());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get organization by ID")
    public ResponseEntity<OrganizationDTO> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                orgService.getOrganizationById(id));
    }

    @PostMapping
    @Operation(
            summary = "Create an organization",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<OrganizationDTO> create(
            @Valid @RequestBody OrganizationDTO dto,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return new ResponseEntity<>(
                orgService.createOrganization(
                        dto, userDetails.getUsername()),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update organization (owner only)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<OrganizationDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationDTO dto,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                orgService.updateOrganization(
                        id, dto, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete organization (owner or admin)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        orgService.deleteOrganization(
                id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/follow")
    @Operation(
            summary = "Follow an organization",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<String> follow(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        orgService.followOrganization(
                id, userDetails.getUsername());
        return ResponseEntity.ok("Following organization");
    }

    @DeleteMapping("/{id}/follow")
    @Operation(
            summary = "Unfollow an organization",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<String> unfollow(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        orgService.unfollowOrganization(
                id, userDetails.getUsername());
        return ResponseEntity.ok("Unfollowed organization");
    }

    @GetMapping("/my")
    @Operation(
            summary = "Get my organizations",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<OrganizationSummaryDTO>>
    getMyOrganizations(
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                orgService.getMyOrganizations(
                        userDetails.getUsername()));
    }

    @GetMapping("/following")
    @Operation(
            summary = "Get organizations I follow",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<OrganizationSummaryDTO>>
    getFollowing(
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                orgService.getFollowedOrganizations(
                        userDetails.getUsername()));
    }
}