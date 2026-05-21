package com.shubham.event_manager.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private Long id;
    private Long eventId;
    private String eventTitle;
    private Long userId;
    private String userName;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating minimum is 1")
    @Max(value = 5, message = "Rating maximum is 5")
    private Integer rating;

    @Size(max = 1000,
            message = "Comment cannot exceed 1000 characters")
    private String comment;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}