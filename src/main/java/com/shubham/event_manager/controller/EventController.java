package com.shubham.event_manager.controller;


import com.shubham.event_manager.document.EventDocument;
import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Event Management",
        description = "APIs for creating, updating, deleting and searching events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    @Operation(summary = "Get all events",
            description = "Returns all events. Result is cached in Redis.")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<EventDTO> getEventById(
            @Parameter(description = "Event ID") @PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new event",
            description = "Creates event, indexes in Elasticsearch, sends confirmation email.")
    public ResponseEntity<EventDTO> createEvent(
            @Valid @RequestBody EventDTO eventDTO,
            @RequestParam(defaultValue = "admin@events.com") String userEmail) {
        return new ResponseEntity<>(
                eventService.createEvent(eventDTO, userEmail),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing event")
    public ResponseEntity<EventDTO> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Fuzzy search events",
            description = "Searches via Elasticsearch. Tolerates typos automatically.")
    public ResponseEntity<List<EventDocument>> searchEvents(
            @Parameter(description = "Search query — typos allowed")
            @RequestParam String q) {
        return ResponseEntity.ok(eventService.searchEvents(q));
    }
}
