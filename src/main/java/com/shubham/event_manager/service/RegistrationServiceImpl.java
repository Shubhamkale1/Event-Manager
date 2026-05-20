package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.RegistrationDTO;
import com.shubham.event_manager.entity.*;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl
        implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


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

    private RegistrationDTO toDTO(
            Registration reg) {
        return RegistrationDTO.builder()
                .id(reg.getId())
                .eventId(reg.getEvent().getId())
                .eventTitle(reg.getEvent().getTitle())
                .eventLocation(
                        reg.getEvent().getLocation())
                .eventDate(reg.getEvent().getEventDate())
                .userId(reg.getUser().getId())
                .userName(reg.getUser().getName())
                .userEmail(reg.getUser().getEmail())
                .status(reg.getStatus())
                .registeredAt(reg.getRegisteredAt())
                .cancelledAt(reg.getCancelledAt())
                .build();
    }

    @Override
    @Transactional
    public RegistrationDTO registerForEvent(
            Long eventId, String userEmail) {

        User user = getUser(userEmail);
        Event event = getEvent(eventId);

        // Guard 1 — already registered
        if (registrationRepository
                .existsByUserAndEvent(user, event)) {
            throw new IllegalArgumentException(
                    "Already registered for this event");
        }

        // Guard 2 — event date passed
        if (event.getEventDate()
                .isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Cannot register for a past event");
        }

        // Guard 3 — atomic capacity check
        // Returns 1 if space available
        // Returns 0 if event is full
        // This is thread-safe — no race condition
        if (event.getCapacity() != null) {
            int updated = registrationRepository
                    .incrementRegistrationCount(eventId);

            if (updated == 0) {
                throw new IllegalArgumentException(
                        "Event is full. " +
                                "No spots remaining.");
            }
        }

        // All guards passed — create registration
        Registration registration =
                Registration.builder()
                        .user(user)
                        .event(event)
                        .status(RegistrationStatus.CONFIRMED)
                        .build();

        Registration saved =
                registrationRepository
                        .save(registration);

        notificationService.notifyRegistrationConfirmed(saved);

        log.info("{} registered for event: {}",
                userEmail, event.getTitle());

        return toDTO(saved);
    }

    @Override
    @Transactional
    public void cancelRegistration(
            Long eventId, String userEmail) {

        User user = getUser(userEmail);
        Event event = getEvent(eventId);

        Registration registration =
                registrationRepository
                        .findByUserAndEvent(user, event)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Registration not found"));

        // Guard — already cancelled
        if (registration.getStatus()
                == RegistrationStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Registration already cancelled");
        }

        // Update status
        registration.setStatus(
                RegistrationStatus.CANCELLED);
        registration.setCancelledAt(
                LocalDateTime.now());
        registrationRepository.save(registration);

        // Free up the spot atomically
        registrationRepository
                .decrementRegistrationCount(eventId);

        log.info("{} cancelled registration for: {}",
                userEmail, event.getTitle());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationDTO> getMyRegistrations(
            String userEmail) {
        User user = getUser(userEmail);
        return registrationRepository
                .findByUserOrderByRegisteredAtDesc(user)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationDTO>
    getEventRegistrations(
            Long eventId,
            String requestingUserEmail) {

        Event event = getEvent(eventId);
        User requestingUser =
                getUser(requestingUserEmail);

        // Only event organizer or admin can see
        // full attendee list
        boolean isAdmin = requestingUser
                .getRole().equals("ADMIN");
        boolean isOrganizer = event.getOrganization()
                != null
                && event.getOrganization()
                .getOwner().getId()
                .equals(requestingUser.getId());

        if (!isAdmin && !isOrganizer) {
            throw new AccessDeniedException(
                    "Only the event organizer or " +
                            "admin can view attendees");
        }

        return registrationRepository
                .findByEventOrderByRegisteredAtAsc(event)
                .stream()
                .filter(r -> r.getStatus()
                        == RegistrationStatus.CONFIRMED)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserRegistered(
            Long eventId, String userEmail) {
        User user = getUser(userEmail);
        Event event = getEvent(eventId);
        return registrationRepository
                .findByUserAndEvent(user, event)
                .map(r -> r.getStatus()
                        == RegistrationStatus.CONFIRMED)
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public long getConfirmedCount(Long eventId) {
        Event event = getEvent(eventId);
        return registrationRepository
                .countByEventAndStatus(
                        event,
                        RegistrationStatus.CONFIRMED);
    }
}