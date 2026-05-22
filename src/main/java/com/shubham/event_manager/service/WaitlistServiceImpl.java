package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.WaitlistDTO;
import com.shubham.event_manager.entity.*;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.repository.*;
import com.shubham.event_manager.service.WaitlistService;
import com.shubham.event_manager.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaitlistServiceImpl
        implements WaitlistService {

    private final WaitlistRepository waitlistRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository
            registrationRepository;
    private final NotificationService
            notificationService;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + email));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found: " + eventId));
    }

    private WaitlistDTO toDTO(WaitlistEntry w) {
        return WaitlistDTO.builder()
                .id(w.getId())
                .eventId(w.getEvent().getId())
                .eventTitle(w.getEvent().getTitle())
                .userId(w.getUser().getId())
                .userName(w.getUser().getName())
                .position(w.getPosition())
                .status(w.getStatus())
                .joinedAt(w.getJoinedAt())
                .build();
    }

    @Override
    @Transactional
    public WaitlistDTO joinWaitlist(
            Long eventId, String userEmail) {

        User user = getUser(userEmail);
        Event event = getEvent(eventId);

        // Guard 1 — event must be full
        if (!event.isFull()) {
            throw new IllegalArgumentException(
                    "Event is not full. " +
                            "Register directly instead.");
        }

        // Guard 2 — already on waitlist
        if (waitlistRepository.existsByUserAndEvent(
                user, event)) {
            throw new IllegalArgumentException(
                    "Already on the waitlist");
        }

        // Guard 3 — already registered
        if (registrationRepository
                .existsByUserAndEvent(user, event)) {
            throw new IllegalArgumentException(
                    "Already registered for this event");
        }

        // Calculate next position
        Integer maxPosition = waitlistRepository
                .findMaxPosition(event);
        int nextPosition = maxPosition != null
                ? maxPosition + 1 : 1;

        WaitlistEntry entry = WaitlistEntry.builder()
                .user(user)
                .event(event)
                .position(nextPosition)
                .status("WAITING")
                .build();

        WaitlistEntry saved =
                waitlistRepository.save(entry);

        log.info("{} joined waitlist for event {} " +
                        "at position {}",
                userEmail, eventId, nextPosition);

        return toDTO(saved);
    }

    @Override
    @Transactional
    public void leaveWaitlist(
            Long eventId, String userEmail) {

        User user = getUser(userEmail);
        Event event = getEvent(eventId);

        WaitlistEntry entry = waitlistRepository
                .findByUserAndEvent(user, event)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Not on waitlist for this event"));

        waitlistRepository.delete(entry);
        log.info("{} left waitlist for event {}",
                userEmail, eventId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WaitlistDTO> getEventWaitlist(
            Long eventId) {
        Event event = getEvent(eventId);
        return waitlistRepository
                .findByEventAndStatusOrderByPositionAsc(
                        event, "WAITING")
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WaitlistDTO> getMyWaitlist(
            String userEmail) {
        User user = getUser(userEmail);
        return waitlistRepository.findAll()
                .stream()
                .filter(w -> w.getUser().getId()
                        .equals(user.getId()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void promoteFromWaitlist(Long eventId) {
        Event event = getEvent(eventId);

        if (event.isFull()) {
            log.info("Event {} still full, " +
                            "cannot promote from waitlist",
                    eventId);
            return;
        }

        waitlistRepository
                .findFirstByEventAndStatusOrderByPositionAsc(
                        event, "WAITING")
                .ifPresent(entry -> {
                    entry.setStatus("NOTIFIED");
                    waitlistRepository.save(entry);

                    // Create notification for waitlisted user
                    log.info(
                            "Promoted {} from waitlist " +
                                    "for event {}. " +
                                    "Position was: {}",
                            entry.getUser().getEmail(),
                            eventId,
                            entry.getPosition());
                });
    }
}