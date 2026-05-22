package com.shubham.event_manager.controller;

import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.service.BookmarkService;
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
@Tag(name = "Bookmarks",
        description = "Save events for later")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{id}/bookmark")
    @Operation(
            summary = "Bookmark an event",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<String> addBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        bookmarkService.addBookmark(
                id, userDetails.getUsername());
        return ResponseEntity.ok("Event bookmarked");
    }

    @DeleteMapping("/{id}/bookmark")
    @Operation(
            summary = "Remove bookmark",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<String> removeBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        bookmarkService.removeBookmark(
                id, userDetails.getUsername());
        return ResponseEntity.ok("Bookmark removed");
    }

    @GetMapping("/bookmarks")
    @Operation(
            summary = "Get my bookmarked events",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<List<EventDTO>>
    getMyBookmarks(
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                bookmarkService.getMyBookmarks(
                        userDetails.getUsername()));
    }

    @GetMapping("/{id}/bookmark-status")
    @Operation(
            summary = "Check if event is bookmarked",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<Boolean>
    isBookmarked(
            @PathVariable Long id,
            @AuthenticationPrincipal
            UserDetails userDetails) {
        return ResponseEntity.ok(
                bookmarkService.isBookmarked(
                        id, userDetails.getUsername()));
    }
}