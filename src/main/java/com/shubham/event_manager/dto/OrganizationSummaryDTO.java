package com.shubham.event_manager.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationSummaryDTO {
    private Long id;
    private String name;
    private String location;
    private int followerCount;
}