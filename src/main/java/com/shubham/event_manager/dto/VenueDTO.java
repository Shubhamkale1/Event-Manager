package com.shubham.event_manager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueDTO {

    private Long id;

    @NotBlank(message = "Venue name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    private String state;
    private String pincode;
    private Integer capacity;

    // WRITE_ONLY — client can send, never returned back
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private BigDecimal latitude;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private BigDecimal longitude;

    // This replaces showing raw coordinates
    // Generated from coordinates — shown to user as clickable map link
    private String mapUrl;

    private LocalDateTime createdAt;
}