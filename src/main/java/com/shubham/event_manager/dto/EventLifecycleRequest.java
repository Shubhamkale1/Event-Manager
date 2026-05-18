package com.shubham.event_manager.dto;

import lombok.Data;

@Data
public class EventLifecycleRequest {

    // Optional — only needed for cancellation
    private String cancellationReason;
}