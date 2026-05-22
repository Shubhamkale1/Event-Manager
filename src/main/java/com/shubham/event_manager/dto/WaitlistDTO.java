package com.shubham.event_manager.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitlistDTO {
    private Long id;
    private Long eventId;
    private String eventTitle;
    private Long userId;
    private String userName;
    private Integer position;
    private String status;
    private LocalDateTime joinedAt;
}