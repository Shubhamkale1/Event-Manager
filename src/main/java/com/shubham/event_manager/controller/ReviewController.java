package com.shubham.event_manager.controller;

import com.shubham.event_manager.dto.ReviewDTO;
import com.shubham.event_manager.service.ReviewService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Reviews",
        description = "Event reviews and ratings")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{id}/reviews")
    @Operation(
            summary = "Submit a review",
            description = "Only for completed events " +
                    "you registered for",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<ReviewDTO> createReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewDTO dto,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return new ResponseEntity<>(
                reviewService.createReview(
                        id, dto,
                        userDetails.getUsername()),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}/reviews")
    @Operation(summary = "Get all reviews for an event")
    public ResponseEntity<List<ReviewDTO>>
    getEventReviews(@PathVariable Long id) {
        return ResponseEntity.ok(
                reviewService.getEventReviews(id));
    }

    @GetMapping("/{id}/rating")
    @Operation(
            summary = "Get average rating for an event"
    )
    public ResponseEntity<Map<String, Object>>
    getEventRating(@PathVariable Long id) {
        return ResponseEntity.ok(
                reviewService.getEventRating(id));
    }

    @PutMapping("/reviews/{reviewId}")
    @Operation(
            summary = "Update my review",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewDTO dto,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                reviewService.updateReview(
                        reviewId, dto,
                        userDetails.getUsername()));
    }

    @DeleteMapping("/reviews/{reviewId}")
    @Operation(
            summary = "Delete a review",
            description = "Owner or admin only",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        reviewService.deleteReview(
                reviewId,
                userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-reviews")
    @Operation(
            summary = "Get my reviews",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<List<ReviewDTO>>
    getMyReviews(
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                reviewService.getMyReviews(
                        userDetails.getUsername()));
    }
}