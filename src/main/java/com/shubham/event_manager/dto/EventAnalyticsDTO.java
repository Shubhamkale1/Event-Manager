package com.shubham.event_manager.dto;

import com.shubham.event_manager.entity.EventStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventAnalyticsDTO {

    private Long eventId;
    private String eventTitle;
    private EventStatus status;
    private Integer capacity;
    private long confirmedRegistrations;
    private long cancelledRegistrations;
    private int spotsRemaining;
    private Double averageRating;
    private long totalReviews;
    private double registrationRate;
}