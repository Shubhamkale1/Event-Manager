package com.shubham.event_manager.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    // Organizer dashboard
    private long totalEventsCreated;
    private long publishedEvents;
    private long cancelledEvents;
    private long completedEvents;
    private long totalRegistrations;
    private long totalUniqueAttendees;
    private double averageRatingAcrossEvents;

    // Per-event breakdown
    private List<EventAnalyticsDTO> eventBreakdown;
}