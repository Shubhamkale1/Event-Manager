package com.shubham.event_manager.service.impl;

import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.dto.EventLifecycleRequest;
import com.shubham.event_manager.entity.*;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.mapper.EventMapper;
import com.shubham.event_manager.repository.*;
import com.shubham.event_manager.service.EventLifecycleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventLifecycleServiceImpl
        implements EventLifecycleService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository
            registrationRepository;
    private final EventMapper eventMapper;

    // ── Helpers ────────────────────────────────────────

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found: " + eventId));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + email));
    }

    private void checkOwnership(
            Event event, User user) {

        boolean isAdmin = user.getRole()
                .equals("ADMIN");
        boolean isOrganizer = event.getOrganization()
                != null
                && event.getOrganization()
                .getOwner().getId()
                .equals(user.getId());

        // If no organization — creator check
        // We compare event creator via organization
        // or fall back to admin only
        if (!isAdmin && !isOrganizer) {
            throw new AccessDeniedException(
                    "Only the event organizer or " +
                            "admin can perform this action");
        }
    }

    private void validateTransition(
            Event event,
            EventStatus newStatus) {

        if (!event.canTransitionTo(newStatus)) {
            throw new IllegalArgumentException(
                    "Cannot transition event from "
                            + event.getStatus()
                            + " to " + newStatus
                            + ". Invalid state transition.");
        }
    }

    // ── Lifecycle Methods ──────────────────────────────

    @Override
    @Transactional
    @CacheEvict(value = {"events", "event"},
            allEntries = true)
    public EventDTO publishEvent(
            Long eventId, String userEmail) {

        Event event = getEvent(eventId);
        User user = getUser(userEmail);

        checkOwnership(event, user);
        validateTransition(event,
                EventStatus.PUBLISHED);

        event.setStatus(EventStatus.PUBLISHED);
        event.setPublishedAt(LocalDateTime.now());

        Event saved = eventRepository.save(event);
        log.info("Event {} published by {}",
                eventId, userEmail);

        return eventMapper.toDTO(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"events", "event"},
            allEntries = true)
    public EventDTO cancelEvent(
            Long eventId,
            String userEmail,
            EventLifecycleRequest request) {

        Event event = getEvent(eventId);
        User user = getUser(userEmail);

        checkOwnership(event, user);
        validateTransition(event,
                EventStatus.CANCELLED);

        event.setStatus(EventStatus.CANCELLED);
        event.setCancelledAt(LocalDateTime.now());

        if (request != null
                && request.getCancellationReason()
                != null) {
            event.setCancellationReason(
                    request.getCancellationReason());
        }

        Event saved = eventRepository.save(event);

        // Notify all registered users
        notifyRegisteredUsers(saved);

        log.info("Event {} cancelled by {}. Reason: {}",
                eventId, userEmail,
                event.getCancellationReason());

        return eventMapper.toDTO(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"events", "event"},
            allEntries = true)
    public EventDTO completeEvent(
            Long eventId, String userEmail) {

        Event event = getEvent(eventId);
        User user = getUser(userEmail);

        checkOwnership(event, user);
        validateTransition(event,
                EventStatus.COMPLETED);

        event.setStatus(EventStatus.COMPLETED);
        event.setCompletedAt(LocalDateTime.now());

        Event saved = eventRepository.save(event);
        log.info("Event {} marked as completed by {}",
                eventId, userEmail);

        return eventMapper.toDTO(saved);
    }

    // ── Private: Notify registered users ──────────────

    private void notifyRegisteredUsers(Event event) {
        List<Registration> registrations =
                registrationRepository
                        .findByEventOrderByRegisteredAtAsc(
                                event);

        long confirmedCount = registrations.stream()
                .filter(r -> r.getStatus()
                        == RegistrationStatus.CONFIRMED)
                .count();

        log.info(
                "Event {} cancelled. {} registered " +
                        "users will be notified. " +
                        "Reason: {}",
                event.getId(),
                confirmedCount,
                event.getCancellationReason());

        // In Phase 5 — publish Kafka message here
        // For now — log the notification
        // Notification system comes in next feature
        registrations.stream()
                .filter(r -> r.getStatus()
                        == RegistrationStatus.CONFIRMED)
                .forEach(r ->
                        log.info(
                                "NOTIFY: {} — Event '{}' " +
                                        "has been cancelled. Reason: {}",
                                r.getUser().getEmail(),
                                event.getTitle(),
                                event.getCancellationReason()
                        )
                );
    }
}