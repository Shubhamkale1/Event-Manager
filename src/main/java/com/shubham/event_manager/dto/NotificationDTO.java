package com.shubham.event_manager.dto;

import com.shubham.event_manager.entity.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

    private Long id;
    private NotificationType type;
    private String title;
    private String message;
    private boolean isRead;
    private String entityType;
    private Long entityId;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}