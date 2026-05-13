package com.shubham.event_manager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {

    @JsonProperty(access =
    JsonProperty.Access.READ_ONLY)
    private Long id;

    private String name;

    @JsonProperty(access =
    JsonProperty.Access.READ_ONLY)
    private String email;

    private String bio;

    private String phone;

    private String city;

    @JsonProperty(access =
    JsonProperty.Access.READ_ONLY)
    private String role;

    @JsonProperty(access =
    JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access =
    JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
}
