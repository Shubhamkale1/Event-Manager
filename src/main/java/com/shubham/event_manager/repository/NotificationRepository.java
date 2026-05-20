package com.shubham.event_manager.repository;

import com.shubham.event_manager.entity.Notification;
import com.shubham.event_manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification>
    findByUserOrderByCreatedAtDesc(User user);

    List<Notification>
    findByUserAndIsReadFalse(User user);

    long countByUserAndIsReadFalse(User user);

    @Modifying
    @Query("""
        UPDATE Notification n
        SET n.isRead = true,
            n.readAt = CURRENT_TIMESTAMP
        WHERE n.user = :user
        AND n.isRead = false
        """)
    int markAllAsRead(@Param("user") User user);

    @Modifying
    @Query("""
        UPDATE Notification n
        SET n.isRead = true,
            n.readAt = CURRENT_TIMESTAMP
        WHERE n.id = :id
        AND n.user = :user
        """)
    int markAsRead(
            @Param("id") Long id,
            @Param("user") User user);
}