package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.NotificationDTO;
import com.shubham.event_manager.entity.*;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.repository.*;
import com.shubham.event_manager.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl
        implements NotificationService {

    private final NotificationRepository
            notificationRepository;
    private final UserRepository userRepository;

    // ── Helper ─────────────────────────────────────────

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + email));
    }

    private NotificationDTO toDTO(
            Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .type(n.getType())
                .title(n.getTitle())
                .message(n.getMessage())
                .isRead(n.isRead())
                .entityType(n.getEntityType())
                .entityId(n.getEntityId())
                .createdAt(n.getCreatedAt())
                .readAt(n.getReadAt())
                .build();
    }

    private Notification createNotification(
            User user,
            NotificationType type,
            String title,
            String message,
            String entityType,
            Long entityId) {

        return Notification.builder()
                .user(user)
                .type(type)
                .title(title)
                .message(message)
                .entityType(entityType)
                .entityId(entityId)
                .build();
    }

    // ── Reading ────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO>
    getMyNotifications(String email) {
        User user = getUser(email);
        return notificationRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO>
    getUnreadNotifications(String email) {
        User user = getUser(email);
        return notificationRepository
                .findByUserAndIsReadFalse(user)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(String email) {
        User user = getUser(email);
        return notificationRepository
                .countByUserAndIsReadFalse(user);
    }

    // ── Actions ────────────────────────────────────────

    @Override
    @Transactional
    public void markAsRead(
            Long notificationId, String email) {
        User user = getUser(email);
        int updated = notificationRepository
                .markAsRead(notificationId, user);

        if (updated == 0) {
            throw new ResourceNotFoundException(
                    "Notification not found or " +
                            "does not belong to you");
        }
    }

    @Override
    @Transactional
    public void markAllAsRead(String email) {
        User user = getUser(email);
        int updated = notificationRepository
                .markAllAsRead(user);
        log.info("Marked {} notifications as read for {}",
                updated, email);
    }

    @Override
    @Transactional
    public void deleteNotification(
            Long notificationId, String email) {
        User user = getUser(email);
        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found: "
                                                + notificationId));

        if (!notification.getUser().getId()
                .equals(user.getId())) {
            throw new AccessDeniedException(
                    "Cannot delete another user's " +
                            "notification");
        }

        notificationRepository
                .delete(notification);
    }

    // ── Creating ───────────────────────────────────────

    @Override
    @Transactional
    public void notifyEventCancelled(
            Event event,
            List<Registration> registrations) {

        String reason = event.getCancellationReason()
                != null
                ? " Reason: " + event.getCancellationReason()
                : "";

        registrations.stream()
                .filter(r -> r.getStatus()
                        == RegistrationStatus.CONFIRMED)
                .forEach(r -> {
                    Notification n = createNotification(
                            r.getUser(),
                            NotificationType.EVENT_CANCELLED,
                            "Event Cancelled",
                            "The event '" + event.getTitle()
                                    + "' you registered for " +
                                    "has been cancelled." + reason,
                            "EVENT",
                            event.getId()
                    );
                    notificationRepository.save(n);
                    log.info(
                            "Notification created for {} — " +
                                    "event {} cancelled",
                            r.getUser().getEmail(),
                            event.getId());
                });
    }

    @Override
    @Transactional
    public void notifyRegistrationConfirmed(
            Registration registration) {
        Notification n = createNotification(
                registration.getUser(),
                NotificationType.REGISTRATION_CONFIRMED,
                "Registration Confirmed",
                "You are registered for '"
                        + registration.getEvent().getTitle()
                        + "'. See you there!",
                "EVENT",
                registration.getEvent().getId()
        );
        notificationRepository.save(n);
    }

    @Override
    @Transactional
    public void notifyNewFollower(
            Organization org, User follower) {
        Notification n = createNotification(
                org.getOwner(),
                NotificationType.NEW_FOLLOWER,
                "New Follower",
                follower.getName()
                        + " started following "
                        + org.getName(),
                "ORGANIZATION",
                org.getId()
        );
        notificationRepository.save(n);
    }

    @Override
    @Transactional
    public void notifyNewEventFromOrg(
            Event event, Organization org) {

        List<OrganizationFollower> followers =
                org.getFollowers();

        followers.forEach(f -> {
            Notification n = createNotification(
                    f.getUser(),
                    NotificationType
                            .NEW_EVENT_FROM_FOLLOWED_ORG,
                    "New Event from "
                            + org.getName(),
                    org.getName()
                            + " posted a new event: '"
                            + event.getTitle() + "'",
                    "EVENT",
                    event.getId()
            );
            notificationRepository.save(n);
        });

        log.info(
                "Notified {} followers of org {} " +
                        "about new event {}",
                followers.size(),
                org.getName(),
                event.getTitle());
    }

    @Override
    @Transactional
    public void notifyWelcome(User user) {
        Notification n = createNotification(
                user,
                NotificationType.WELCOME,
                "Welcome to Event Manager!",
                "Hi " + user.getName()
                        + "! Welcome to the platform. " +
                        "Browse events, follow organizations, " +
                        "and start registering.",
                null,
                null
        );
        notificationRepository.save(n);
        log.info("Welcome notification created for {}",
                user.getEmail());
    }
}