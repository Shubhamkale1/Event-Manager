package com.shubham.event_manager.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRegisteredMessage {

    private Long eventId;
    private String eventTitle;
    private String eventLocation;
    private LocalDateTime eventDate;
    private String userEmail;
    private String userName;
    // Single user who just registered
    // Email Service sends confirmation to this user
}