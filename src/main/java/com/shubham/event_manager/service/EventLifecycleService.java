package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.dto.EventLifecycleRequest;

public interface EventLifecycleService {

    EventDTO publishEvent(Long eventId,
                          String userEmail);

    EventDTO cancelEvent(Long eventId,
                         String userEmail,
                         EventLifecycleRequest request);

    EventDTO completeEvent(Long eventId,
                           String userEmail);
}