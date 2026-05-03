package com.shubham.event_manager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    // Optional — user can provide manually
    // If not provided, system tries to geocode automatically
    private BigDecimal latitude;
    private BigDecimal longitude;

    private LocalDateTime createdAt;
}