package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.*;
import com.shubham.event_manager.entity.*;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.repository.*;
import com.shubham.event_manager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl
        implements DashboardService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository
            registrationRepository;
    private final ReviewRepository reviewRepository;
    private final OrganizationRepository
            orgRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardDTO getOrganizerDashboard(
            String userEmail) {

        User user = getUser(userEmail);

        // Get all events created by this user
        // through their organizations
        List<Organization> orgs =
                orgRepository.findByOwner(user);

        List<Event> allEvents = orgs.stream()
                .flatMap(org ->
                        org.getEvents().stream())
                .collect(Collectors.toList());

        long published = allEvents.stream()
                .filter(e -> e.getStatus()
                        == EventStatus.PUBLISHED)
                .count();

        long cancelled = allEvents.stream()
                .filter(e -> e.getStatus()
                        == EventStatus.CANCELLED)
                .count();

        long completed = allEvents.stream()
                .filter(e -> e.getStatus()
                        == EventStatus.COMPLETED)
                .count();

        long totalRegs = allEvents.stream()
                .mapToLong(e ->
                        registrationRepository
                                .countByEventAndStatus(
                                        e,
                                        RegistrationStatus.CONFIRMED))
                .sum();

        List<EventAnalyticsDTO> breakdown =
                allEvents.stream()
                        .map(this::toAnalyticsDTO)
                        .collect(Collectors.toList());

        double avgRating = allEvents.stream()
                .mapToDouble(e -> {
                    Double avg = reviewRepository
                            .findAverageRatingByEvent(e);
                    return avg != null ? avg : 0.0;
                })
                .average()
                .orElse(0.0);

        return DashboardDTO.builder()
                .totalEventsCreated(allEvents.size())
                .publishedEvents(published)
                .cancelledEvents(cancelled)
                .completedEvents(completed)
                .totalRegistrations(totalRegs)
                .averageRatingAcrossEvents(
                        Math.round(avgRating * 10.0) / 10.0)
                .eventBreakdown(breakdown)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAdminDashboard() {
        long totalEvents = eventRepository.count();
        long totalUsers = userRepository.count();
        long totalOrgs = orgRepository.count();

        long publishedEvents = eventRepository
                .findAllPublished().size();

        return Map.of(
                "totalEvents", totalEvents,
                "publishedEvents", publishedEvents,
                "totalUsers", totalUsers,
                "totalOrganizations", totalOrgs
        );
    }

    @Override
    @Transactional(readOnly = true)
    public EventAnalyticsDTO getEventAnalytics(
            Long eventId, String userEmail) {

        User user = getUser(userEmail);
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found: " + eventId));

        boolean isAdmin = user.getRole()
                .equals("ADMIN");
        boolean isOrganizer = event.getOrganization()
                != null
                && event.getOrganization()
                .getOwner().getId()
                .equals(user.getId());

        if (!isAdmin && !isOrganizer) {
            throw new AccessDeniedException(
                    "Only organizer or admin can view " +
                            "event analytics");
        }

        return toAnalyticsDTO(event);
    }

    private EventAnalyticsDTO toAnalyticsDTO(
            Event event) {

        long confirmed = registrationRepository
                .countByEventAndStatus(
                        event,
                        RegistrationStatus.CONFIRMED);

        long cancelled = registrationRepository
                .countByEventAndStatus(
                        event,
                        RegistrationStatus.CANCELLED);

        Double avgRating = reviewRepository
                .findAverageRatingByEvent(event);

        long reviewCount = reviewRepository
                .countByEvent(event);

        double regRate = event.getCapacity() != null
                && event.getCapacity() > 0
                ? (double) confirmed
                / event.getCapacity() * 100
                : 0.0;

        return EventAnalyticsDTO.builder()
                .eventId(event.getId())
                .eventTitle(event.getTitle())
                .status(event.getStatus())
                .capacity(event.getCapacity())
                .confirmedRegistrations(confirmed)
                .cancelledRegistrations(cancelled)
                .spotsRemaining(event.getSpotsRemaining())
                .averageRating(avgRating != null
                        ? Math.round(avgRating * 10.0) / 10.0
                        : null)
                .totalReviews(reviewCount)
                .registrationRate(
                        Math.round(regRate * 10.0) / 10.0)
                .build();
    }
}