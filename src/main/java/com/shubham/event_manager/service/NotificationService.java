package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.NotificationDTO;
import com.shubham.event_manager.entity.*;

import java.util.List;

public interface NotificationService {

    // Reading
    List<NotificationDTO> getMyNotifications(
            String email);

    List<NotificationDTO> getUnreadNotifications(
            String email);

    long getUnreadCount(String email);

    // Actions
    void markAsRead(Long notificationId,
                    String email);

    void markAllAsRead(String email);

    void deleteNotification(Long notificationId,
                            String email);

    // Creating — called internally by other services
    void notifyEventCancelled(Event event,
                              List<Registration> registrations);

    void notifyRegistrationConfirmed(
            Registration registration);

    void notifyNewFollower(
            Organization org, User follower);

    void notifyNewEventFromOrg(
            Event event,
            Organization org);

    void notifyWelcome(User user);
}