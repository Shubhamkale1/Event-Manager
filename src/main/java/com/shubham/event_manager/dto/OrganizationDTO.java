package com.shubham.event_manager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationDTO {

    private Long id;

    @NotBlank(message = "Organization name is required")
    private String name;

    private String description;
    private String website;
    private String location;

    // Read only — set by system
    private Long ownerId;
    private String ownerName;
    private int followerCount;
    private boolean isFollowing;
    private LocalDateTime createdAt;
}