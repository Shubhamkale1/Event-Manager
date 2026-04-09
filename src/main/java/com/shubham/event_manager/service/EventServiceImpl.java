package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.EventDTO;
import com.shubham.event_manager.entity.Event;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.mapper.EventMapper;
import com.shubham.event_manager.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
                "Event not found with id: " + id));
        return eventMapper.toDTO(event);
    }

    @Override
    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = eventMapper.toEntity(eventDTO);
        Event saved = eventRepository.save(event);
        return eventMapper.toDTO(saved);
    }

    @Override
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found with id:" + id));
        eventMapper.updateEventFromDTO(eventDTO,existing);
        Event saved = eventRepository.save(existing);
        return eventMapper.toDTO(saved);


    }

    @Override
    public void deleteEvent(Long id) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found with id:" + id));
        eventRepository.delete(existing);
    }
}
