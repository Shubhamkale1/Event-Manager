package com.shubham.event_manager.service;

import com.shubham.event_manager.entity.Event;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long id, Event updatedEvent) {
        Event existing = getEventById(id);
        existing.setTitle(updatedEvent.getTitle());
        existing.setDescription(updatedEvent.getDescription());
        existing.setEventDate(updatedEvent.getEventDate());
        existing.setLocation(updatedEvent.getLocation());
        existing.setCapacity(updatedEvent.getCapacity());

        return eventRepository.save(existing);
    }

    @Override
    public void deleteEvent(Long id) {
        Event existing = getEventById(id);
        eventRepository.delete(existing);

    }
}
