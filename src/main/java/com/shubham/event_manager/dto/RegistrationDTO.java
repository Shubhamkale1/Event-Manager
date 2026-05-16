package com.shubham.event_manager.dto;

import com.shubham.event_manager.entity.RegistrationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationDTO {

    private Long id;
    private Long eventId;
    private String eventTitle;
    private String eventLocation;
    private LocalDateTime eventDate;
    private Long userId;
    private String userName;
    private String userEmail;
    private RegistrationStatus status;
    private LocalDateTime registeredAt;
    private LocalDateTime cancelledAt;
}