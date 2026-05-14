package com.shubham.event_manager.service;


import com.shubham.event_manager.document.EventDocument;
import com.shubham.event_manager.dto.EventDTO;

import java.util.List;

public interface EventService {

    List<EventDTO> getAllEvents();

    EventDTO getEventById(Long id);

    EventDTO createEvent(EventDTO eventDTO, String userEmail);

    EventDTO updateEvent(Long id, EventDTO eventDTO);

    void deleteEvent(Long id);

    List<EventDocument> searchEvents(String query);

    List<EventDocument> getEventsByCategory(
            String category);
}
