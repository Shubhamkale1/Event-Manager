package com.shubham.event_manager.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationFollowerId
        implements Serializable {

    private Long userId;
    private Long organizationId;
}