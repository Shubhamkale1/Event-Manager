package com.shubham.event_manager.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCancelledMessage {

    private Long eventId;
    private String eventTitle;
    private String cancellationReason;
    private LocalDateTime cancelledAt;
    private List<String> registeredUserEmails;
    // List of all registered user emails
    // Email Service reads this and sends to each one
}