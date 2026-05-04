package com.shubham.event_manager.controller;

import com.shubham.event_manager.dto.VenueDTO;
import com.shubham.event_manager.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
@Tag(name = "Venues",
        description = "Venue management and discovery")
public class VenueController {

    private final VenueService venueService;

    @GetMapping
    @Operation(summary = "Get all venues")
    public ResponseEntity<List<VenueDTO>> getAllVenues() {
        return ResponseEntity.ok(
                venueService.getAllVenues());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get venue by ID")
    public ResponseEntity<VenueDTO> getVenueById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                venueService.getVenueById(id));
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "Get venues by city")
    public ResponseEntity<List<VenueDTO>> getByCity(
            @PathVariable String city) {
        return ResponseEntity.ok(
                venueService.getVenuesByCity(city));
    }

    @GetMapping("/capacity/{min}")
    @Operation(
            summary = "Get venues by minimum capacity",
            description = "Find venues that can hold at least N people"
    )
    public ResponseEntity<List<VenueDTO>> getByCapacity(
            @PathVariable Integer min) {
        return ResponseEntity.ok(
                venueService.getVenuesByMinCapacity(min));
    }

    @PostMapping
    @Operation(
            summary = "Create a venue",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<VenueDTO> createVenue(
            @Valid @RequestBody VenueDTO venueDTO) {
        return new ResponseEntity<>(
                venueService.createVenue(venueDTO),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a venue",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<VenueDTO> updateVenue(
            @PathVariable Long id,
            @Valid @RequestBody VenueDTO venueDTO) {
        return ResponseEntity.ok(
                venueService.updateVenue(id, venueDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a venue",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteVenue(
            @PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
